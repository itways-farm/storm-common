package com.stormfarm.common.exception;

/** The account is temporarily locked after repeated failures; mapped to HTTP 423. */
public class AccountLockedException extends RuntimeException {
    public AccountLockedException(String message) {
        super(message);
    }
}
