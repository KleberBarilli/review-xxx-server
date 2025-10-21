package com.idealizer.review_x.application.user.usecases;

import com.idealizer.review_x.application.user.responses.AccessTokenResponse;
import com.idealizer.review_x.security.jwt.JwtUtils;
import com.idealizer.review_x.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.Map;

@Service
public class MobileSignInUseCase {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    // Apenas para informar o app; o TTL real do token hoje vem de jwtUtils.jwtExpirationMs
    @Value("${auth.mobile.ttl.days:60}")
    private int mobileTtlDays;

    public MobileSignInUseCase(AuthenticationManager authenticationManager, JwtUtils jwtUtils) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
    }

    public AccessTokenResponse execute(String identifier, String password) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(identifier, password)
        );
        UserDetailsImpl p = (UserDetailsImpl) auth.getPrincipal();

        // Usa a assinatura existente (1 argumento)
        long ttlMillis = java.time.Duration.ofDays(mobileTtlDays).toMillis();
        String access = jwtUtils.generateToken(
                p,
                ttlMillis,
                Map.of("aud", "mobile", "typ", "access")
        );

        List<String> roles = p.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        return new AccessTokenResponse(
                access,
                mobileTtlDays * 24L * 3600L, // apenas informativo por enquanto
                new AccessTokenResponse.User(p.getId().toHexString(), p.getUsername(), roles)
        );
    }
}