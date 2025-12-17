package com.idealizer.review_x.application.user.usecases;

import com.idealizer.review_x.security.services.UserDetailsImpl;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;



@Service
public class SignInUseCase {

    private final AuthenticationManager authenticationManager;

    public SignInUseCase(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    public UserDetailsImpl execute(String identifier, String password) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(identifier, password)
        );
        SecurityContextHolder.getContext().setAuthentication(auth);
        return (UserDetailsImpl) auth.getPrincipal();
    }
}