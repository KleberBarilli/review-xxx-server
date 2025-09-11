package com.idealizer.review_x.config;

import com.idealizer.review_x.application.user.usecases.FindUserByEmailUseCase;
import com.idealizer.review_x.application.user.usecases.SignUpUseCase;
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

import java.io.IOException;



@Component
public class OAuth2LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private final SignUpUseCase signUpUseCase;
    private final FindUserByEmailUseCase findUserByEmailUseCase;
    private final JwtUtils jwtUtils;

    @Value("${app.frontend.url}")
    private String frontendUrl;

    public OAuth2LoginSuccessHandler(SignUpUseCase signUpUseCase,
                                     FindUserByEmailUseCase findUserByEmailUseCase,
                                     JwtUtils jwtUtils) {
        this.signUpUseCase = signUpUseCase;
        this.findUserByEmailUseCase = findUserByEmailUseCase;
        this.jwtUtils = jwtUtils;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        try {
            OAuth2AuthenticationToken oauth = (OAuth2AuthenticationToken) authentication;
            var principal = (DefaultOAuth2User) oauth.getPrincipal();
            var attrs = principal.getAttributes();

            String email = String.valueOf(attrs.getOrDefault("email", ""));
            String username = email.contains("@") ? email.substring(0, email.indexOf('@')) : "user";
            var userOpt = findUserByEmailUseCase.execute(email);
            if (userOpt.isEmpty()) {
                signUpUseCase.execute(new SignupRequestDTO(username, email, null, null, null, null));
            }
            var user = findUserByEmailUseCase.execute(email)
                    .orElseThrow(() -> new IllegalStateException("User not found after signup"));

            var userDetails = new UserDetailsImpl(
                    user.getId(),
                    user.getName(),
                    null,
                    user.getEmail(),
                    null
            );

            String jwtToken = jwtUtils.generateToken(userDetails);

            var cookie = ResponseCookie.from("LV_AT", jwtToken)
                    .httpOnly(true)
                    .secure(false)
                    .sameSite("Lax")
                    .path("/")
                    .maxAge(15 * 60)
                    .build();
            response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());


            this.setAlwaysUseDefaultTargetUrl(true);
            this.setDefaultTargetUrl(frontendUrl + "/redirect");
            super.onAuthenticationSuccess(request, response, authentication);
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_FOUND);
            response.sendRedirect(frontendUrl + "/auth/error?code=token_issue_failed");
        }

    }
}
