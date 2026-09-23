package com.stormfarm.common.web;

import com.stormfarm.common.dto.response.ApiResponse;
import com.stormfarm.common.exception.AccountLockedException;
import com.stormfarm.common.exception.BadRequestException;
import com.stormfarm.common.exception.ConflictException;
import com.stormfarm.common.exception.EntityNotFoundException;
import com.stormfarm.common.exception.IntegrationException;
import com.stormfarm.common.exception.RateLimitExceededException;
import com.stormfarm.common.exception.UnauthorizedException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CommonExceptionHandlerTest {

    private final CommonExceptionHandler handler = new CommonExceptionHandler();

    @Test
    void domainExceptionsMapToTheirStatusCodes() {
        assertStatus(HttpStatus.NOT_FOUND, handler.handleNotFound(new EntityNotFoundException("no such device")));
        assertStatus(HttpStatus.BAD_REQUEST, handler.handleBadRequest(new BadRequestException("bad")));
        assertStatus(HttpStatus.CONFLICT, handler.handleConflict(new ConflictException("dup")));
        assertStatus(HttpStatus.UNAUTHORIZED, handler.handleUnauthorized(new UnauthorizedException("who")));
        assertStatus(HttpStatus.LOCKED, handler.handleAccountLocked(new AccountLockedException("locked")));
        assertStatus(HttpStatus.TOO_MANY_REQUESTS, handler.handleRateLimit(new RateLimitExceededException("slow down")));
        assertStatus(HttpStatus.BAD_GATEWAY, handler.handleIntegration(new IntegrationException("stf down")));
    }

    @Test
    void springSecurityExceptionsMapToTheirStatusCodes() {
        assertStatus(HttpStatus.UNAUTHORIZED, handler.handleBadCredentials(new BadCredentialsException("Invalid credentials")));
        assertStatus(HttpStatus.LOCKED, handler.handleLocked(new LockedException("locked")));
        assertStatus(HttpStatus.FORBIDDEN, handler.handleAccessDenied(new AccessDeniedException("nope")));
    }

    @Test
    void malformedRequestFamilyMapsWithoutFallingThroughToFiveHundred() {
        // A wrong-typed path/query value, the wrong verb, a non-JSON body, and an
        // unknown/unrouted path are all client mistakes — each must map to a 4xx
        // here rather than reach the catch-all and be reported as a 500 at ERROR.
        assertStatus(HttpStatus.BAD_REQUEST, handler.handleTypeMismatch(
                new org.springframework.web.method.annotation.MethodArgumentTypeMismatchException(
                        "notanumber", Long.class, "id", null, new NumberFormatException())));
        assertStatus(HttpStatus.METHOD_NOT_ALLOWED, handler.handleMethodNotSupported(
                new org.springframework.web.HttpRequestMethodNotSupportedException("PUT")));
        assertStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE, handler.handleUnsupportedMedia(
                new org.springframework.web.HttpMediaTypeNotSupportedException("text/plain")));
        assertStatus(HttpStatus.NOT_FOUND, handler.handleNoResource(
                new org.springframework.web.servlet.resource.NoResourceFoundException(
                        org.springframework.http.HttpMethod.GET, "/api/v1/test-jobs/11", "")));
    }

    @Test
    void unknownPathMessageDoesNotEchoTheRequestedPath() {
        // The 404 body for an unknown path must not reflect the requested URL back.
        ResponseEntity<ApiResponse<Void>> response = handler.handleNoResource(
                new org.springframework.web.servlet.resource.NoResourceFoundException(
                        org.springframework.http.HttpMethod.GET, "/api/v1/secret-probe", ""));
        assertStatus(HttpStatus.NOT_FOUND, response);
        assertFalse(response.getBody().getMessage().contains("secret-probe"));
    }

    @Test
    void unexpectedExceptionsDoNotLeakTheirMessage() {
        ResponseEntity<ApiResponse<Void>> response = handler.handleGeneral(new RuntimeException("jdbc password=hunter2"));

        assertStatus(HttpStatus.INTERNAL_SERVER_ERROR, response);
        assertEquals("An internal server error occurred", response.getBody().getMessage());
    }

    @Test
    void bodyAlwaysHasTheApiResponseShape() {
        ResponseEntity<ApiResponse<Void>> response = handler.handleConflict(new ConflictException("already reserved"));

        ApiResponse<Void> body = response.getBody();
        assertNotNull(body);
        assertFalse(body.isSuccess());
        assertEquals("already reserved", body.getMessage());
        assertNotNull(body.getErrors());
        assertTrue(body.getErrors().isEmpty());
    }

    private static void assertStatus(HttpStatus expected, ResponseEntity<?> response) {
        assertEquals(expected, response.getStatusCode());
    }
}
