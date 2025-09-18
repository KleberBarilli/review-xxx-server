package com.idealizer.review_x.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class RefreshOriginFilter extends OncePerRequestFilter {

    @Value("${app.security.refresh-path}")
    private String refreshPath;

    // mesmos domínios do front (http/https conforme ambiente)
    @Value("#{'${app.security.allowed-origins}'.split(',')}")
    private List<String> allowedOrigins;

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws ServletException, IOException {

        String uri = req.getRequestURI();
        if (uri.equals(refreshPath)) {
            // só POST
            if (!"POST".equalsIgnoreCase(req.getMethod())) {
                res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
                return;
            }
            // Origin/Referer precisa estar na whitelist
            String origin = req.getHeader("Origin");
            String referer = req.getHeader("Referer");
            boolean ok = (origin != null && allowedOrigins.contains(origin))
                    || (referer != null && allowedOrigins.stream().anyMatch(referer::startsWith));

            if (!ok) {
                res.setStatus(HttpServletResponse.SC_FORBIDDEN);
                return;
            }
        }

        chain.doFilter(req, res);
    }
}
