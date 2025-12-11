package com.idealizer.review_x.config;

import com.idealizer.review_x.application.user.usecases.FindUserByEmailUseCase;
import com.idealizer.review_x.application.user.usecases.SignUpUseCase;
import com.idealizer.review_x.infra.http.controllers.user.dto.SignupRequestDTO;
import com.idealizer.review_x.domain.core.tokens.CookieUtil;
import com.idealizer.review_x.domain.core.tokens.RefreshService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
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
    private final RefreshService refreshService;
    private final CookieUtil cookies;

    @Value("${app.frontend.url}")
    private String frontendUrl;

    @Value("${auth.refresh.days:60}")
    private int refreshDays;

    private final String COOKIE_DOMAIN = System.getenv("COOKIE_DOMAIN");

    public OAuth2LoginSuccessHandler(
            SignUpUseCase signUpUseCase,
            FindUserByEmailUseCase findUserByEmailUseCase,
            RefreshService refreshService,
            CookieUtil cookies
    ) {
        this.signUpUseCase = signUpUseCase;
        this.findUserByEmailUseCase = findUserByEmailUseCase;
        this.refreshService = refreshService;
        this.cookies = cookies;
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

            var user = findUserByEmailUseCase.execute(email)
                    .orElseThrow(() -> new IllegalStateException("User not found after signup"));

            var pair = refreshService.issue(user.getId().toHexString(), null, refreshDays);

            cookies.setRefreshCookie(response, pair.cookieValue(), refreshDays * 24 * 3600, COOKIE_DOMAIN);

            String targetUrl = UriComponentsBuilder.fromHttpUrl(frontendUrl + "/redirect")
                    .queryParam("is_new_user", isNewUser)
                    .build()
                    .toUriString();

            this.setAlwaysUseDefaultTargetUrl(true);
            this.setDefaultTargetUrl(targetUrl);

            super.onAuthenticationSuccess(request, response, authentication);

        } catch (Exception e) {
            logger.error("OAuth2 Login Error", e);
            response.sendRedirect(frontendUrl + "/auth/error?code=token_issue_failed");
        }
    }

    private String generateTempUsername(String email) {
        String prefix = email.split("@")[0];
        String suffix = UUID.randomUUID().toString().substring(0, 4);
        return prefix + "_" + suffix;
    }
}