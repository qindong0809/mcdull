package io.gitee.mcdull.tools.web.domain;

import io.gitee.dqcer.mcdull.framework.base.support.VO;
import lombok.Data;

/**
 * Progress of a database import task.
 * <p>
 * This object doubles as the mutable task state held in memory: the worker thread updates it while
 * request threads read it. Fields are volatile so updates are visible across threads. A reader may
 * observe two fields from adjacent updates, which is harmless for a progress display and avoids the
 * extra snapshot object.
 *
 * @author dqcer
 */
@Data
public class ImportProgressVO implements VO {

    private static final long serialVersionUID = 1L;

    private volatile String taskId;

    private volatile ImportPhaseEnum phase;

    /**
     * Percentage of the dump file already fed to the mysql client. Only meaningful while the phase
     * is IMPORTING, because the masking phase has no measurable unit of work.
     */
    private volatile int percent;

    private volatile long processedBytes;

    private volatile long totalBytes;

    /**
     * Database name found in the dump file.
     */
    private volatile String sourceDatabaseName;

    /**
     * Versioned schema actually created by this import, this is the one to connect to.
     */
    private volatile String databaseName;

    /**
     * Masking sql output on success, or the failure reason when the phase is FAILED.
     */
    private volatile String message;

    private volatile long startMillis;

    /**
     * Completion timestamp, zero while the task is still running.
     */
    private volatile long endMillis;

    /**
     * Wall clock duration, frozen once the task completes.
     *
     * @return elapsed milliseconds
     */
    public long getElapsedMillis() {
        long end = this.endMillis;
        return (end > 0 ? end : System.currentTimeMillis()) - this.startMillis;
    }
}
