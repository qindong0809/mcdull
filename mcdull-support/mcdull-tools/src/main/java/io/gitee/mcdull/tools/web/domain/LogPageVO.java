package io.gitee.mcdull.tools.web.domain;

import io.gitee.dqcer.mcdull.framework.base.support.VO;
import lombok.Data;

import java.util.List;

/**
 * One page of history, read backwards from the tail of the file.
 *
 * @author dqcer
 */
@Data
public class LogPageVO implements VO {

    private static final long serialVersionUID = 1L;

    private String file;

    /**
     * Lines in file order, oldest first.
     */
    private List<String> lines;

    /**
     * Byte offset where this window starts. Send it back as the cursor to load the previous page.
     * Null means the head of the file was reached and there is nothing older.
     */
    private Long nextCursor;

    private long fileSize;

    private long scannedBytes;

    /**
     * True when the scan hit the byte budget before filling the page, so older matches may exist.
     */
    private boolean truncated;
}
