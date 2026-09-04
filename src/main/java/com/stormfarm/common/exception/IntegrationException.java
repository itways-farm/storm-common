package com.stormfarm.common.exception;

/** An external system (STF, Appium, mail, ...) failed or is unreachable; mapped to HTTP 502. */
public class IntegrationException extends RuntimeException {
    public IntegrationException(String message) {
        super(message);
    }

    public IntegrationException(String message, Throwable cause) {
        super(message, cause);
    }
}
