package com.idealizer.review_x.application.user.usecases;

import com.idealizer.review_x.application.user.responses.LoginResponse;
import com.idealizer.review_x.security.jwt.JwtUtils;
import com.idealizer.review_x.security.services.UserDetailsImpl;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class SignInUseCase {
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    public SignInUseCase(AuthenticationManager authenticationManager, JwtUtils jwtUtils) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
    }

    public LoginResponse execute(String username, String password, String locale) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password));
        SecurityContextHolder.getContext().setAuthentication(auth);

        UserDetailsImpl principal = (UserDetailsImpl) auth.getPrincipal();

        String jwtToken = jwtUtils.generateToken(principal, locale); // <-- passa o principal
        return new LoginResponse(jwtToken);

    }
}
