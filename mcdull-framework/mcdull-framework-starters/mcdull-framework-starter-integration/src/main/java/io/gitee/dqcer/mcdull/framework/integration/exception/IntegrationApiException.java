package io.gitee.dqcer.mcdull.framework.integration.exception;

import lombok.Getter;

/**
 * Unified exception for integration API call failures.
 *
 * @author dqcer
 * @since 1.0.0
 */
@Getter
public class IntegrationApiException extends RuntimeException {

    private final int httpStatus;
    private final String clientName;

    public IntegrationApiException(String clientName, String message) {
        super(message);
        this.clientName = clientName;
        this.httpStatus = 0;
    }

    public IntegrationApiException(String clientName, int httpStatus, String message) {
        super(message);
        this.clientName = clientName;
        this.httpStatus = httpStatus;
    }

    public IntegrationApiException(String clientName, String message, Throwable cause) {
        super(message, cause);
        this.clientName = clientName;
        this.httpStatus = 0;
    }
}
