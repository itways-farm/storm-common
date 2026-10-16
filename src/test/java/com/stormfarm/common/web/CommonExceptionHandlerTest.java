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
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
    void signInServiceOutageIsA503WithAFixedMessage() {
        // The exception's message is for operators; the body never repeats it.
        ResponseEntity<ApiResponse<Void>> response = handler.handleAuthenticationService(
                new AuthenticationServiceException("Directory authentication is not configured correctly",
                        new IllegalStateException("ldap://10.0.0.5 bind DN cn=admin")));

        assertStatus(HttpStatus.SERVICE_UNAVAILABLE, response);
        assertFalse(response.getBody().isSuccess());
        assertEquals("Sign-in is temporarily unavailable. Try again shortly.", response.getBody().getMessage());
        assertTrue(response.getBody().getErrors().isEmpty());
    }

    @Test
    void signInServiceOutageWinsOverTheCatchAllWhenDispatched() throws Exception {
        // Through Spring MVC's own resolution: without the specific mapping the
        // Exception catch-all answered 500 "An internal server error occurred".
        MockMvc mvc = MockMvcBuilders.standaloneSetup(new OutageController())
                .setControllerAdvice(handler)
                .build();

        mvc.perform(post("/directory-down"))
                .andExpect(status().isServiceUnavailable())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Sign-in is temporarily unavailable. Try again shortly."));
        // An account-store failure during sign-in is the same outage (subclass).
        mvc.perform(post("/account-store-down"))
                .andExpect(status().isServiceUnavailable())
                .andExpect(jsonPath("$.message").value("Sign-in is temporarily unavailable. Try again shortly."));
    }

    @RestController
    static class OutageController {
        @PostMapping("/directory-down")
        void directoryDown() {
            throw new AuthenticationServiceException("Directory authentication is temporarily unavailable");
        }

        @PostMapping("/account-store-down")
        void accountStoreDown() {
            throw new InternalAuthenticationServiceException("connection refused: jdbc:postgresql://db:5432");
        }
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
