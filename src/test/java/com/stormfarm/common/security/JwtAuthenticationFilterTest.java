package com.stormfarm.common.security;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class JwtAuthenticationFilterTest {

    @Test
    void bearerHeaderIsAlwaysAccepted() {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/v1/devices");
        request.addHeader("Authorization", "Bearer abc.def.ghi");

        assertEquals("abc.def.ghi", JwtAuthenticationFilter.extractToken(request));
    }

    @Test
    void queryTokenIsIgnoredOnOrdinaryRestCalls() {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/v1/devices");
        request.setParameter("token", "abc.def.ghi");

        assertNull(JwtAuthenticationFilter.extractToken(request));
    }

    @Test
    void queryTokenIsAcceptedForWebSocketUpgrade() {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/ws/stream/42");
        request.addHeader("Upgrade", "websocket");
        request.setParameter("token", "abc.def.ghi");

        assertEquals("abc.def.ghi", JwtAuthenticationFilter.extractToken(request));
    }

    @Test
    void queryTokenIsAcceptedForServerSentEvents() {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/v1/devices/events");
        request.addHeader("Accept", "text/event-stream");
        request.setParameter("token", "abc.def.ghi");

        assertEquals("abc.def.ghi", JwtAuthenticationFilter.extractToken(request));
    }

    @Test
    void headerWinsOverQueryParameter() {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/ws/stream/42");
        request.addHeader("Upgrade", "websocket");
        request.addHeader("Authorization", "Bearer from-header");
        request.setParameter("token", "from-query");

        assertEquals("from-header", JwtAuthenticationFilter.extractToken(request));
    }

    @Test
    void blankQueryTokenIsTreatedAsAbsent() {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/ws/stream/42");
        request.addHeader("Upgrade", "websocket");
        request.setParameter("token", "   ");

        assertNull(JwtAuthenticationFilter.extractToken(request));
    }
}
