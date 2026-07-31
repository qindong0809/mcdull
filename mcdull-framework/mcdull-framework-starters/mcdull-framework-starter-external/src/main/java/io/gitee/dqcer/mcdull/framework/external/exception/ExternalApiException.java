package io.gitee.dqcer.mcdull.framework.external.exception;

import lombok.Getter;

/**
 * Unified exception for external API call failures.
 *
 * @author dqcer
 * @since 1.0.0
 */
@Getter
public class ExternalApiException extends RuntimeException {

    private final int httpStatus;
    private final String clientName;

    public ExternalApiException(String clientName, String message) {
        super(message);
        this.clientName = clientName;
        this.httpStatus = 0;
    }

    public ExternalApiException(String clientName, int httpStatus, String message) {
        super(message);
        this.clientName = clientName;
        this.httpStatus = httpStatus;
    }

    public ExternalApiException(String clientName, String message, Throwable cause) {
        super(message, cause);
        this.clientName = clientName;
        this.httpStatus = 0;
    }
}
