package com.idealizer.review_x.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.logging.Logger;

@Component
public class OAuth2LoginFailureHandler implements AuthenticationFailureHandler {
    private final Logger logger = Logger.getLogger(OAuth2LoginFailureHandler.class.getName());

    @Value("${app.frontend.url}")
    private String frontendUrl;

    public  OAuth2LoginFailureHandler() {
    }
    @Override
    public void onAuthenticationFailure(
            HttpServletRequest request, HttpServletResponse response, AuthenticationException ex)
            throws IOException {
        logger.info("OAuth2 failure: {}" + ex.getMessage());

        response.setStatus(HttpServletResponse.SC_FOUND);
        response.sendRedirect(frontendUrl + "/error?code=oauth_failed");
    }
}