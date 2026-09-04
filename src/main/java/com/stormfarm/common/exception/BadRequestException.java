package com.stormfarm.common.exception;

/** The request cannot be processed as given; mapped to HTTP 400. */
public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }
}
