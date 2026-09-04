package com.stormfarm.common.exception;

/** Too many requests from this caller; mapped to HTTP 429. */
public class RateLimitExceededException extends RuntimeException {
    public RateLimitExceededException(String message) {
        super(message);
    }
}
