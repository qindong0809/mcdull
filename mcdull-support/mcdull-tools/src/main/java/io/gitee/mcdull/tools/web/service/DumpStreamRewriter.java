package io.gitee.mcdull.tools.web.service;

import io.gitee.dqcer.mcdull.framework.base.exception.BusinessException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.function.LongConsumer;

/**
 * Streams a mysql dump while redirecting it into a different database.
 * <p>
 * Statements that act on a database as a whole are neutralised so the dump can only ever touch the
 * versioned schema this tool created. Everything else is passed through byte for byte. The scan works
 * on raw bytes and only decodes short lines, so escaped binary values cannot be corrupted by a
 * charset round trip.
 *
 * @author dqcer
 */
final class DumpStreamRewriter {

    private static final int COPY_BUFFER_SIZE = 64 * 1024;

    /**
     * A line longer than this cannot be one of the statements handled here, so it is streamed
     * through without buffering. This keeps memory bounded on extended insert dumps.
     */
    private static final int LINE_PEEK_LIMIT = 4096;

    private static final byte LINE_FEED = (byte) '\n';

    private static final String USE_KEYWORD = "USE ";

    /**
     * Only the head of a line needs normalising to recognise the leading keyword.
     */
    private static final int KEYWORD_SCAN_WIDTH = 64;

    private static final String[] CREATE_DATABASE_KEYWORDS = {"CREATE DATABASE", "CREATE SCHEMA"};

    /**
     * A dump exported with --add-drop-database carries these. Executing one would delete a real
     * database on the target server, which is exactly what this tool must never do.
     */
    private static final String[] DROP_DATABASE_KEYWORDS = {"DROP DATABASE", "DROP SCHEMA"};

    private final String targetDatabase;

    /**
     * Database named by the first USE statement, used to reject multi database dumps.
     */
    private String firstSourceDatabase;

    DumpStreamRewriter(String targetDatabase) {
        this.targetDatabase = targetDatabase;
    }

    /**
     * Whether the line starts a CREATE DATABASE or CREATE SCHEMA statement.
     *
     * @param line line to test
     * @return true when it creates a database
     */
    static boolean isCreateDatabase(String line) {
        return startsWithAny(line, CREATE_DATABASE_KEYWORDS);
    }

    /**
     * Whether the line starts a USE statement.
     *
     * @param line line to test
     * @return true when it is a USE statement
     */
    static boolean isUse(String line) {
        return startsWithAny(line, new String[]{USE_KEYWORD});
    }

    /**
     * Extract the option suffix of a CREATE DATABASE statement, everything after the database
     * identifier. Reusing it verbatim keeps the original character set and collation without having
     * to parse them.
     *
     * @param line a CREATE DATABASE statement
     * @return the suffix, or null when the identifier is not back quoted
     */
    static String extractCreateDatabaseSuffix(String line) {
        int opening = line.indexOf('`');
        int closing = opening < 0 ? -1 : line.indexOf('`', opening + 1);
        return closing < 0 ? null : line.substring(closing + 1).trim();
    }

    /**
     * Read the database named by a USE statement.
     *
     * @param line a USE statement
     * @return the database name, never null but possibly blank
     */
    static String parseUseTarget(String line) {
        // The caller already established this is a USE statement, so skip the three keyword letters
        // rather than matching an exact "USE " spelling.
        String value = line.trim().substring(3);
        int semicolon = value.indexOf(';');
        if (semicolon >= 0) {
            // Cut at the statement terminator so a trailing inline comment is dropped.
            value = value.substring(0, semicolon);
        }
        return value.replaceAll("[`\\s]", "");
    }

    /**
     * Copy the dump into the target stream, redirecting it at the versioned schema.
     *
     * @param input          dump content
     * @param output         destination, normally the mysql client stdin
     * @param bytesReadTotal receives the cumulative number of bytes read from the input
     * @throws IOException when reading or writing fails
     */
    void rewriteAndCopy(InputStream input, OutputStream output, LongConsumer bytesReadTotal)
            throws IOException {
        byte[] buffer = new byte[COPY_BUFFER_SIZE];
        ByteArrayOutputStream pending = new ByteArrayOutputStream(256);
        boolean streamingLongLine = false;
        long readTotal = 0L;
        int read;
        while ((read = input.read(buffer)) != -1) {
            readTotal += read;
            bytesReadTotal.accept(readTotal);
            int cursor = 0;
            while (cursor < read) {
                int lineFeed = indexOfLineFeed(buffer, cursor, read);
                if (streamingLongLine) {
                    int end = lineFeed < 0 ? read : lineFeed + 1;
                    output.write(buffer, cursor, end - cursor);
                    cursor = end;
                    streamingLongLine = lineFeed < 0;
                    continue;
                }
                if (lineFeed < 0) {
                    pending.write(buffer, cursor, read - cursor);
                    cursor = read;
                    if (pending.size() > LINE_PEEK_LIMIT) {
                        // Too long to be a statement handled here, stop buffering it.
                        pending.writeTo(output);
                        pending.reset();
                        streamingLongLine = true;
                    }
                    continue;
                }
                pending.write(buffer, cursor, lineFeed + 1 - cursor);
                cursor = lineFeed + 1;
                this.writeLine(output, pending.toByteArray());
                pending.reset();
            }
        }
        if (pending.size() > 0) {
            // Trailing line without a line feed.
            this.writeLine(output, pending.toByteArray());
        }
        output.flush();
    }

    private void writeLine(OutputStream output, byte[] line) throws IOException {
        // ISO-8859-1 maps every byte to one character, so decoding and encoding is lossless here.
        String text = new String(line, StandardCharsets.ISO_8859_1);
        String replacement = null;
        if (isUse(text)) {
            this.checkSingleDatabase(text);
            replacement = this.rewriteUse(text);
        } else if (isCreateDatabase(text)) {
            // The schema is created up front with the original options, so this statement is dropped.
            // Keeping it would recreate the original database name as an empty schema.
            replacement = comment("CREATE DATABASE replaced by the versioned schema");
        } else if (startsWithAny(text, DROP_DATABASE_KEYWORDS)) {
            // Never let a dump delete a database on the target server.
            replacement = comment("DROP DATABASE removed, it would delete a real database");
        }
        if (replacement == null) {
            output.write(line);
            return;
        }
        output.write(replacement.getBytes(StandardCharsets.ISO_8859_1));
    }

    /**
     * Reject dumps covering more than one database: they would all be merged into a single schema.
     */
    private void checkSingleDatabase(String line) {
        String source = parseUseTarget(line);
        if (firstSourceDatabase == null) {
            firstSourceDatabase = source;
            return;
        }
        if (!firstSourceDatabase.equals(source)) {
            throw new BusinessException("The dump covers more than one database ("
                    + firstSourceDatabase + ", " + source + "), import them one at a time");
        }
    }

    /**
     * Rebuild the USE statement so it points at the versioned schema, keeping the original
     * indentation and whatever follows the terminator.
     */
    private String rewriteUse(String line) {
        int keyword = line.toUpperCase(Locale.ROOT).indexOf("USE");
        int semicolon = line.indexOf(';', keyword);
        String tail = semicolon < 0 ? System.lineSeparator() : line.substring(semicolon);
        return line.substring(0, keyword) + "USE `" + targetDatabase + "`" + tail;
    }

    private static String comment(String reason) {
        return "-- [mcdull] " + reason + System.lineSeparator();
    }

    private static boolean startsWithAny(String line, String[] keywords) {
        String head = normalizedHead(line);
        for (String keyword : keywords) {
            if (head.startsWith(keyword)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Upper cased head of the line with whitespace runs collapsed, so a tab or a double space between
     * keywords cannot make a statement slip through unrecognised.
     */
    private static String normalizedHead(String line) {
        String trimmed = line.trim();
        int limit = Math.min(trimmed.length(), KEYWORD_SCAN_WIDTH);
        return trimmed.substring(0, limit).replaceAll("\\s+", " ").toUpperCase(Locale.ROOT);
    }

    private static int indexOfLineFeed(byte[] buffer, int from, int to) {
        for (int index = from; index < to; index++) {
            if (buffer[index] == LINE_FEED) {
                return index;
            }
        }
        return -1;
    }
}
