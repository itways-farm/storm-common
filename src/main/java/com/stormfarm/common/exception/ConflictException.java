package com.stormfarm.common.exception;

/** The request conflicts with current state (duplicate, illegal transition); mapped to HTTP 409. */
public class ConflictException extends RuntimeException {
    public ConflictException(String message) {
        super(message);
    }
}
