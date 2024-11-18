package io.gitee.dqcer.mcdull.business.common.dump.util;

import java.io.PrintWriter;

/**
 * Utility class used to print comments in dump file.
 *
 * @author dqcer
 * @since 2024/11/18
 */
public class SqlCommentUtility {
    // line comment start
    private static final String COMMENT_PREFIX = "-- ";
    // separator line
    private static final String COMMENT_SEPARATOR = "----------------------------------------------------";
    // block comment start
    private static final String COMMENT_BLOCK_PREFIX = "/*";
    // block comment end
    private static final String COMMENT_BLOCK_SUFFIX = "*/";
    // block comment indent
    private static final String COMMENT_BLOCK_PADDING = "  ";

    /**
     * Print a line comment.
     *
     * @param printWriter where to print
     * @param comment     comment to be printed
     */
    public static void printCommentLine(PrintWriter printWriter, String comment) {
        printWriter.println(COMMENT_PREFIX + comment);
    }

    /**
     * Print an empty line comment.
     *
     * @param printWriter where to print
     */
    public static void printCommentLine(PrintWriter printWriter) {
        printWriter.println(COMMENT_PREFIX);
    }

    /**
     * Print a separator line comment.
     *
     * @param printWriter where to print
     */
    public static void printCommentLineSeparator(PrintWriter printWriter) {
        printWriter.println(COMMENT_PREFIX + COMMENT_SEPARATOR);
    }

    /**
     * Used to print comment before dump structure and records.
     *
     * @param printWriter where to print
     * @param comment     comment to be printed
     */
    public static void printCommentHeader(PrintWriter printWriter, String comment) {
        printCommentLine(printWriter);
        printCommentLine(printWriter, comment);
        printCommentLine(printWriter);
    }

    /**
     * Print empty line.
     *
     * @param printWriter where to print
     */
    public static void printEmptyLine(PrintWriter printWriter) {
        printWriter.println();
    }

    /**
     * Print comment black.
     *
     * @param printWriter where to print
     * @param comment     comment to be printed
     */
    public static void printCommentBlockLine(PrintWriter printWriter, String comment) {
        printWriter.println(COMMENT_BLOCK_PREFIX);
        String[] lines = comment.split("\\r?\\n");
        for (String line : lines) {
            printWriter.println(COMMENT_BLOCK_PADDING + line);
        }
        printWriter.println(COMMENT_BLOCK_SUFFIX);
    }
}
