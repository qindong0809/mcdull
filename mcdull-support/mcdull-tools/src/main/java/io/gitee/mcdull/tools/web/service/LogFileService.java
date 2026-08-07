package io.gitee.mcdull.tools.web.service;

import cn.hutool.core.util.StrUtil;
import io.gitee.dqcer.mcdull.framework.base.exception.BusinessException;
import io.gitee.dqcer.mcdull.framework.base.help.LogHelp;
import io.gitee.mcdull.tools.config.LogProperties;
import io.gitee.mcdull.tools.web.domain.LogFileVO;
import io.gitee.mcdull.tools.web.domain.LogPageVO;
import io.gitee.mcdull.tools.web.service.source.LogReader;
import io.gitee.mcdull.tools.web.service.source.LogSource;
import io.gitee.mcdull.tools.web.service.source.LogSourceRegistry;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

/**
 * Reads log files for the history view.
 * <p>
 * Log files are far too large to page by line number, because counting lines means scanning from the
 * head every time. Paging is therefore done on byte offsets: a page is read backwards from the tail
 * in fixed size blocks and returns the offset it started at, which the caller sends back to fetch
 * the page in front of it. Cost per page is constant no matter how large the file is.
 * <p>
 * Everything here works through {@link LogSource}, so the same algorithm serves a local directory and
 * a remote host.
 *
 * @author dqcer
 */
@Slf4j
@Service
public class LogFileService {

    private static final int BLOCK_SIZE = 64 * 1024;

    private static final int MAX_PAGE_LINES = 2000;

    /**
     * A single line longer than this is clipped instead of buffered, so a file without any line
     * terminator cannot exhaust the heap.
     */
    private static final int MAX_LINE_BYTES = 1024 * 1024;

    private static final byte LF = '\n';

    private static final byte CR = '\r';

    private static final byte[] EMPTY = new byte[0];

    @Resource
    private LogProperties logProperties;

    @Resource
    private LogSourceRegistry logSourceRegistry;

    /**
     * Files available for browsing, newest first.
     *
     * @param target target id, blank uses the default
     * @return readable log files
     */
    public List<LogFileVO> listFiles(String target) {
        return logSourceRegistry.resolve(target).list();
    }

    /**
     * Reads one page of lines ending at {@code cursor}, optionally keeping only the lines that
     * contain {@code keyword}.
     *
     * @param target   target id
     * @param fileName file name, blank means the active file
     * @param cursor   exclusive end offset, null starts at the tail of the file
     * @param limit    maximum lines to return
     * @param keyword  case insensitive substring filter, blank returns every line
     * @return the page plus the cursor for the page in front of it
     */
    public LogPageVO readBackward(String target, String fileName, Long cursor, Integer limit, String keyword) {
        LogSource logSource = logSourceRegistry.resolve(target);
        int pageLines = (limit == null || limit <= 0)
                ? logProperties.getPageLines()
                : Math.min(limit, MAX_PAGE_LINES);
        String needle = StrUtil.isBlank(keyword) ? null : keyword.toLowerCase(Locale.ROOT);
        Charset charset = logSource.charset();

        LogPageVO vo = new LogPageVO();
        vo.setFile(StrUtil.isBlank(fileName) ? logSource.activeFile() : fileName);
        LinkedList<String> lines = new LinkedList<>();

        try (LogReader reader = logSource.open(fileName)) {
            long fileSize = reader.size();
            vo.setFileSize(fileSize);

            long end = cursor == null ? fileSize : Math.max(0L, Math.min(cursor, fileSize));
            end = this.dropTrailingTerminator(reader, end);

            long position = end;
            // Start offset of the oldest line already decoded. Handed back as the next cursor so the
            // following page always begins on a line boundary instead of inside a line.
            long boundary = end;
            long scanned = 0;
            byte[] carry = EMPTY;
            boolean filled = false;
            boolean truncated = false;

            while (position > 0) {
                int blockLength = (int) Math.min(BLOCK_SIZE, position);
                long blockStart = position - blockLength;
                byte[] block = new byte[blockLength];
                reader.readFully(blockStart, block, 0, blockLength);
                scanned += blockLength;

                int segmentEnd = blockLength;
                for (int i = blockLength - 1; i >= 0; i--) {
                    // Splitting on the raw 0x0A byte is safe for UTF-8 and GBK alike, neither uses it
                    // as a continuation byte, so a block boundary can never cut a character in half.
                    if (block[i] != LF) {
                        continue;
                    }
                    String line = this.toLine(block, i + 1, segmentEnd, carry, charset);
                    carry = EMPTY;
                    segmentEnd = i;
                    boundary = blockStart + i + 1;
                    if (this.matches(line, needle)) {
                        lines.addFirst(line);
                        if (lines.size() >= pageLines) {
                            filled = true;
                            break;
                        }
                    }
                }
                if (filled) {
                    break;
                }
                carry = this.prepend(block, segmentEnd, carry);
                position = blockStart;
                if (position > 0 && scanned >= logProperties.getMaxScanBytes()) {
                    truncated = true;
                    break;
                }
            }

            if (!filled && !truncated && position == 0 && carry.length > 0) {
                // The first line of the file has no terminator in front of it.
                String line = this.toLine(EMPTY, 0, 0, carry, charset);
                if (this.matches(line, needle)) {
                    lines.addFirst(line);
                }
                boundary = 0;
            }

            vo.setLines(lines);
            vo.setScannedBytes(scanned);
            vo.setTruncated(truncated);
            vo.setNextCursor(boundary > 0 ? boundary : null);
            return vo;
        } catch (IOException e) {
            LogHelp.error(log, "failed to read log file. file: {}", vo.getFile(), e);
            throw new BusinessException("Failed to read log file " + vo.getFile());
        }
    }

    private boolean matches(String line, String needle) {
        return needle == null || line.toLowerCase(Locale.ROOT).contains(needle);
    }

    /**
     * Moves the window end in front of a single trailing terminator, otherwise the tail of the file
     * and every following cursor would yield one phantom empty line.
     */
    private long dropTrailingTerminator(LogReader reader, long end) throws IOException {
        if (end <= 0) {
            return end;
        }
        byte[] one = new byte[1];
        reader.readFully(end - 1, one, 0, 1);
        return one[0] == LF ? end - 1 : end;
    }

    private String toLine(byte[] block, int from, int to, byte[] carry, Charset charset) {
        int headLength = to - from;
        byte[] buffer = new byte[headLength + carry.length];
        if (headLength > 0) {
            System.arraycopy(block, from, buffer, 0, headLength);
        }
        if (carry.length > 0) {
            System.arraycopy(carry, 0, buffer, headLength, carry.length);
        }
        int length = buffer.length;
        if (length > 0 && buffer[length - 1] == CR) {
            length--;
        }
        return new String(buffer, 0, length, charset);
    }

    private byte[] prepend(byte[] block, int length, byte[] carry) {
        if (length <= 0) {
            return carry;
        }
        if (carry.length >= MAX_LINE_BYTES) {
            // Line already over the cap, keep what was collected and stop growing.
            return carry;
        }
        int keep = Math.min(length, MAX_LINE_BYTES - carry.length);
        byte[] merged = new byte[keep + carry.length];
        System.arraycopy(block, length - keep, merged, 0, keep);
        System.arraycopy(carry, 0, merged, keep, carry.length);
        return merged;
    }
}
