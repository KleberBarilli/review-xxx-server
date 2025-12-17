package com.idealizer.review_x.config;

import com.idealizer.review_x.application.user.usecases.FindUserByEmailUseCase;
import com.idealizer.review_x.application.user.usecases.SignUpUseCase;
import com.idealizer.review_x.application.user.usecases.UpdateLastLoginUseCase;
import com.idealizer.review_x.infra.http.controllers.user.dto.SignupRequestDTO;
import com.idealizer.review_x.security.jwt.JwtUtils;
import com.idealizer.review_x.security.services.UserDetailsImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Component
public class OAuth2LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private final SignUpUseCase signUpUseCase;
    private final FindUserByEmailUseCase findUserByEmailUseCase;
    private final UpdateLastLoginUseCase updateLastLoginUseCase;
    private final JwtUtils jwtUtils;

    @Value("${app.frontend.url}")
    private String frontendUrl;

    public OAuth2LoginSuccessHandler(
            SignUpUseCase signUpUseCase,
            FindUserByEmailUseCase findUserByEmailUseCase,
            UpdateLastLoginUseCase updateLastLoginUseCase,
            JwtUtils jwtUtils
    ) {
        this.signUpUseCase = signUpUseCase;
        this.findUserByEmailUseCase = findUserByEmailUseCase;
        this.updateLastLoginUseCase = updateLastLoginUseCase;
        this.jwtUtils = jwtUtils;
    }

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException, ServletException {
        try {
            OAuth2AuthenticationToken oauth = (OAuth2AuthenticationToken) authentication;
            DefaultOAuth2User principal = (DefaultOAuth2User) oauth.getPrincipal();
            Map<String, Object> attrs = principal.getAttributes();

            String email = String.valueOf(attrs.getOrDefault("email", "")).trim();

            if (email.isBlank()) {
                response.sendRedirect(frontendUrl + "/auth/error?code=missing_email_scope");
                return;
            }

            Optional<?> userOpt = findUserByEmailUseCase.execute(email);
            boolean isNewUser = userOpt.isEmpty();

            if (isNewUser) {
                String tempUsername = generateTempUsername(email);
                String avatarUrl = attrs.containsKey("picture") ? attrs.get("picture").toString() : null;

                signUpUseCase.execute(new SignupRequestDTO(tempUsername, email, null, avatarUrl, null, null));
            }

            var userDomain = findUserByEmailUseCase.execute(email)
                    .orElseThrow(() -> new IllegalStateException("User not found after signup"));

            UserDetailsImpl userDetails = UserDetailsImpl.build(userDomain);
            ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);


            response.addHeader(HttpHeaders.SET_COOKIE, jwtCookie.toString());
            updateLastLoginUseCase.execute(userDetails.getId());

            String targetUrl = UriComponentsBuilder.fromHttpUrl(frontendUrl + "/redirect")
                    .queryParam("is_new_user", isNewUser)
                    .build()
                    .toUriString();

            this.setAlwaysUseDefaultTargetUrl(true);
            this.setDefaultTargetUrl(targetUrl);

            super.onAuthenticationSuccess(request, response, authentication);

        } catch (Exception e) {
            logger.error("[OAuth2] Erro fatal no login OAuth2", e);
            response.sendRedirect(frontendUrl + "/auth/error?code=token_issue_failed");
        }
    }

    private String generateTempUsername(String email) {
        String prefix = email.split("@")[0];
        String suffix = UUID.randomUUID().toString().substring(0, 4);
        return prefix + "_" + suffix;
    }
}