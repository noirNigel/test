package org.example.demomanagementsystemcproject.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Adds security-related HTTP response headers that should be present for all responses.
 */
@Component
public class SecurityHeaderFilter extends OncePerRequestFilter {

    private static final String X_CONTENT_TYPE_OPTIONS = "X-Content-Type-Options";
    private static final String NOSNIFF = "nosniff";

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        if (!response.containsHeader(X_CONTENT_TYPE_OPTIONS)) {
            response.addHeader(X_CONTENT_TYPE_OPTIONS, NOSNIFF);
        }
        filterChain.doFilter(request, response);
    }
}
