package com.idealizer.review_x.application.user.usecases;

import com.idealizer.review_x.application.user.responses.LoginResponse;
import com.idealizer.review_x.domain.core.tokens.RefreshService;
import com.idealizer.review_x.security.jwt.JwtUtils;
import com.idealizer.review_x.security.services.UserDetailsImpl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SignInUseCase {
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final RefreshService refreshService;

    @Value("${auth.refresh.days:60}")
    private int refreshDays;

    public SignInUseCase(AuthenticationManager authenticationManager, JwtUtils jwtUtils, RefreshService refreshService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.refreshService = refreshService;
    }

    public LoginResponse execute(String identifier, String password) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(identifier, password)
        );
        SecurityContextHolder.getContext().setAuthentication(auth);

        UserDetailsImpl principal = (UserDetailsImpl) auth.getPrincipal();

        // Access curto (expiração configurada em JwtUtils.jwtExpirationMs)
        String access = jwtUtils.generateToken(principal);

        // Refresh longo (cookie será setado no controller)
        var pair = refreshService.issue(principal.getId().toHexString(), null, refreshDays);

        List<String> roles = principal.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        return new LoginResponse(
                principal.getId().toHexString(),
                principal.getUsername(),
                roles,
                access,
                pair.cookieValue(),
                refreshDays * 24 * 3600
        );
    }
}