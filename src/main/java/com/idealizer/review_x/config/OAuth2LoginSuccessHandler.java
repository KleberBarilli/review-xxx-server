package com.idealizer.review_x.config;

import com.idealizer.review_x.application.user.usecases.FindUserByEmailUseCase;
import com.idealizer.review_x.application.user.usecases.SignUpUseCase;
import com.idealizer.review_x.infra.http.controllers.user.dto.SignupRequestDTO;
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
import java.io.IOException;
import com.idealizer.review_x.domain.core.tokens.CookieUtil;
import com.idealizer.review_x.domain.core.tokens.RefreshService;
import java.util.Map;
import java.util.Optional;

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
            if (userOpt.isEmpty()) {
                String username = email.contains("@") ? email.substring(0, email.indexOf('@')) : "user";
                signUpUseCase.execute(new SignupRequestDTO(username, email, null, null, null, null));
            }

            var user = findUserByEmailUseCase.execute(email)
                    .orElseThrow(() -> new IllegalStateException("User not found after signup"));

            var pair = refreshService.issue(user.getId().toHexString(), null, refreshDays);
            cookies.setRefreshCookie(response, pair.cookieValue(), refreshDays * 24 * 3600, COOKIE_DOMAIN);


            boolean isHttps = request.isSecure() || "true".equals(System.getenv("COOKIE_SECURE"));
            String name = isHttps ? "__Host-refresh" : "refresh";

            ResponseCookie cookie = ResponseCookie.from(name, pair.cookieValue())
                    .httpOnly(true)
                    .secure(isHttps)
                    .sameSite("Lax")
                    .path("/")
                    .maxAge(refreshDays * 24L * 3600L)
                    .build();

            response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

            this.setAlwaysUseDefaultTargetUrl(true);
            this.setDefaultTargetUrl(frontendUrl + "/redirect");
            super.onAuthenticationSuccess(request, response, authentication);

        } catch (Exception e) {
            logger.error(e);
            response.sendRedirect(frontendUrl + "/auth/error?code=token_issue_failed");
        }
    }
}