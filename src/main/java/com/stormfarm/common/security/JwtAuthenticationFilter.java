package com.stormfarm.common.security;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Authenticates requests carrying a JWT and populates the security context.
 *
 * The token is read from the {@code Authorization: Bearer} header. Only for
 * requests that a browser cannot decorate with headers, namely WebSocket
 * upgrades and Server-Sent Events subscriptions, is a {@code ?token=} query
 * parameter accepted as a fallback. Ordinary REST calls never authenticate
 * from the query string, so tokens do not leak into access logs and
 * {@code Referer} headers.
 */
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    static final String QUERY_TOKEN_PARAM = "token";

    private final JwtService jwtService;

    public JwtAuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String token = extractToken(request);
        if (token == null) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            Claims claims = jwtService.validate(token);
            List<String> roles = claims.get("roles", List.class);
            var authorities = roles.stream()
                    .map(role -> new SimpleGrantedAuthority(role.startsWith("ROLE_") ? role : "ROLE_" + role))
                    .collect(Collectors.toList());

            Long userId = Long.parseLong(claims.getSubject());

            var authentication = new UsernamePasswordAuthenticationToken(userId, null, authorities);
            log.debug("Authenticated user id={}", userId);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (Exception e) {
            log.warn("JWT validation failed for path {}: {}", request.getRequestURI(), e.getMessage());
            SecurityContextHolder.clearContext();
        }
        filterChain.doFilter(request, response);
    }

    /** Package-private for tests. */
    static String extractToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        if (acceptsQueryToken(request)) {
            String queryToken = request.getParameter(QUERY_TOKEN_PARAM);
            if (queryToken != null && !queryToken.isBlank()) {
                return queryToken;
            }
        }
        return null;
    }

    /**
     * WebSocket handshakes ({@code Upgrade: websocket}) and EventSource
     * subscriptions ({@code Accept: text/event-stream}) are the only requests
     * for which the browser API cannot attach an Authorization header.
     */
    static boolean acceptsQueryToken(HttpServletRequest request) {
        String upgrade = request.getHeader("Upgrade");
        if (upgrade != null && upgrade.equalsIgnoreCase("websocket")) {
            return true;
        }
        String accept = request.getHeader("Accept");
        return accept != null && accept.toLowerCase().contains("text/event-stream");
    }
}
