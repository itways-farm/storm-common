package com.stormfarm.common.web;

import com.stormfarm.common.dto.response.ApiResponse;
import com.stormfarm.common.exception.AccountLockedException;
import com.stormfarm.common.exception.BadRequestException;
import com.stormfarm.common.exception.ConflictException;
import com.stormfarm.common.exception.EntityNotFoundException;
import com.stormfarm.common.exception.IntegrationException;
import com.stormfarm.common.exception.RateLimitExceededException;
import com.stormfarm.common.exception.UnauthorizedException;
import jakarta.persistence.EntityExistsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.List;
import java.util.stream.Collectors;

/**
 * The one exception-to-HTTP mapping for every REST service. Every error body
 * has the same shape, {@link ApiResponse} with {@code success=false},
 * {@code message} and {@code errors}, so the web console can treat all
 * services alike.
 *
 * Activation: services whose {@code @SpringBootApplication} class lives under
 * {@code com.stormfarm} pick this advice up by component scan. Services with a
 * narrower root package add {@code @Import(CommonExceptionHandler.class)}.
 *
 * Runs at the lowest precedence so a service can add a
 * {@code @RestControllerAdvice} of its own for domain-specific exceptions
 * (annotate it with a higher {@link Order}) without this class' catch-all
 * {@link Exception} handler getting in first.
 */
@RestControllerAdvice
@Order(Ordered.LOWEST_PRECEDENCE)
public class CommonExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(CommonExceptionHandler.class);

    // -- domain exceptions (com.stormfarm.common.exception) ------------------

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleNotFound(EntityNotFoundException e) {
        return error(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiResponse<Void>> handleBadRequest(BadRequestException e) {
        return error(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ApiResponse<Void>> handleConflict(ConflictException e) {
        return error(HttpStatus.CONFLICT, e.getMessage());
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ApiResponse<Void>> handleUnauthorized(UnauthorizedException e) {
        return error(HttpStatus.UNAUTHORIZED, e.getMessage());
    }

    @ExceptionHandler(AccountLockedException.class)
    public ResponseEntity<ApiResponse<Void>> handleAccountLocked(AccountLockedException e) {
        return error(HttpStatus.LOCKED, e.getMessage());
    }

    @ExceptionHandler(RateLimitExceededException.class)
    public ResponseEntity<ApiResponse<Void>> handleRateLimit(RateLimitExceededException e) {
        return error(HttpStatus.TOO_MANY_REQUESTS, e.getMessage());
    }

    @ExceptionHandler(IntegrationException.class)
    public ResponseEntity<ApiResponse<Void>> handleIntegration(IntegrationException e) {
        log.warn("Integration failure: {}", e.getMessage());
        return error(HttpStatus.BAD_GATEWAY, e.getMessage());
    }

    // -- Spring Security ------------------------------------------------------

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiResponse<Void>> handleBadCredentials(BadCredentialsException e) {
        return error(HttpStatus.UNAUTHORIZED, e.getMessage());
    }

    @ExceptionHandler(LockedException.class)
    public ResponseEntity<ApiResponse<Void>> handleLocked(LockedException e) {
        return error(HttpStatus.LOCKED, e.getMessage());
    }

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<ApiResponse<Void>> handleDisabled(DisabledException e) {
        return error(HttpStatus.FORBIDDEN, e.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<Void>> handleAccessDenied(AccessDeniedException e) {
        return error(HttpStatus.FORBIDDEN, "Access denied: " + e.getMessage());
    }

    // -- persistence ----------------------------------------------------------

    @ExceptionHandler({EntityExistsException.class, DataIntegrityViolationException.class})
    public ResponseEntity<ApiResponse<Void>> handleConstraintViolation(Exception e) {
        log.warn("Data integrity violation: {}", e.getMessage());
        return error(HttpStatus.CONFLICT,
                "A database constraint was violated. This usually means a duplicate name or missing reference.");
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ApiResponse<Void>> handleIllegalState(IllegalStateException e) {
        log.warn("Illegal state: {}", e.getMessage());
        return error(HttpStatus.CONFLICT, e.getMessage());
    }

    // -- request parsing / validation -----------------------------------------

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<Void>> handleMalformedJson(HttpMessageNotReadableException e) {
        log.warn("Malformed JSON in request: {}", e.getMostSpecificCause().getMessage());
        return error(HttpStatus.BAD_REQUEST,
                "Malformed or invalid JSON body: " + e.getMostSpecificCause().getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidation(MethodArgumentNotValidException e) {
        List<String> errors = e.getBindingResult().getFieldErrors().stream()
                .map(fe -> fe.getField() + ": " + fe.getDefaultMessage())
                .collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(HttpStatus.BAD_REQUEST.value(), "Validation failed", errors));
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiResponse<Void>> handleMissingParams(MissingServletRequestParameterException e) {
        return error(HttpStatus.BAD_REQUEST, "Missing parameter: " + e.getParameterName());
    }

    /**
     * A wrong-typed path variable or query param (a non-numeric id, an invalid
     * enum value, an unparseable date) is a client mistake — 400, not a 500
     * catch-all. Names the parameter but never echoes the rejected value back.
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse<Void>> handleTypeMismatch(MethodArgumentTypeMismatchException e) {
        log.warn("Type mismatch for parameter '{}'", e.getName());
        return error(HttpStatus.BAD_REQUEST, "Invalid value for parameter '" + e.getName() + "'");
    }

    /** Wrong HTTP verb for the route. */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiResponse<Void>> handleMethodNotSupported(HttpRequestMethodNotSupportedException e) {
        return error(HttpStatus.METHOD_NOT_ALLOWED, "HTTP method not supported for this endpoint");
    }

    /** Non-JSON / unsupported request content type. */
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ApiResponse<Void>> handleUnsupportedMedia(HttpMediaTypeNotSupportedException e) {
        return error(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "Unsupported content type");
    }

    /**
     * An unknown path is a 404, not a 500. In Spring 6.1 an unmapped request
     * falls through to the resource handler and throws NoResourceFoundException;
     * without this it would reach the catch-all below and be logged at ERROR as
     * "an internal server error", telling operators the service is broken while
     * it is working correctly. Never echoes the requested path back.
     */
    @ExceptionHandler(org.springframework.web.servlet.resource.NoResourceFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleNoResource(
            org.springframework.web.servlet.resource.NoResourceFoundException e) {
        return error(HttpStatus.NOT_FOUND, "The requested resource was not found");
    }

    // -- everything else ------------------------------------------------------

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGeneral(Exception e) {
        log.error("Unhandled exception: {}", e.getMessage(), e);
        return error(HttpStatus.INTERNAL_SERVER_ERROR, "An internal server error occurred");
    }

    private static ResponseEntity<ApiResponse<Void>> error(HttpStatus status, String message) {
        return ResponseEntity.status(status)
                .body(ApiResponse.error(status.value(), message, List.of()));
    }
}
