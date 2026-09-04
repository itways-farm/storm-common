package com.stormfarm.common.exception;

/** Missing or invalid credentials; mapped to HTTP 401. */
public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException(String message) {
        super(message);
    }
}
