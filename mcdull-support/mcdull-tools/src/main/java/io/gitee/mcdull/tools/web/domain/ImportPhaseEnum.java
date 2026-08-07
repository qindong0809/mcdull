package io.gitee.mcdull.tools.web.domain;

/**
 * Phase of a database import task.
 *
 * @author dqcer
 */
public enum ImportPhaseEnum {

    /**
     * Feeding the dump file into the mysql client. Byte level progress is available.
     */
    IMPORTING,

    /**
     * Running the masking sql. Duration is not predictable.
     */
    MASKING,

    /**
     * Imported and masked successfully.
     */
    SUCCESS,

    /**
     * Terminated with an error, see the task message for details.
     */
    FAILED;

    /**
     * Whether the task reached a terminal phase and will no longer change.
     *
     * @return true when no further update is expected
     */
    public boolean isTerminal() {
        return this == SUCCESS || this == FAILED;
    }
}
