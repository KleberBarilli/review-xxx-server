package com.idealizer.review_x.security.jwt;
import com.idealizer.review_x.application.user.usecases.UpdateLastLoginUseCase;
import com.idealizer.review_x.security.services.UserDetailsImpl;
import com.idealizer.review_x.security.services.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;

import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Objects;

public class JwtAuthTokenFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;
    private final UserDetailsServiceImpl userDetailsService;
    private final UpdateLastLoginUseCase updateLastLoginUseCase;

    // Injeção via Construtor
    public JwtAuthTokenFilter(JwtUtils jwtUtils,
                              UserDetailsServiceImpl userDetailsService,
                              UpdateLastLoginUseCase updateLastLoginUseCase) {
        this.jwtUtils = jwtUtils;
        this.userDetailsService = userDetailsService;
        this.updateLastLoginUseCase = updateLastLoginUseCase;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        try {
            String jwt = parseJwt(request);

            if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
                String username = jwtUtils.getUserNameFromJwtToken(jwt);
                UserDetailsImpl userDetails = (UserDetailsImpl) userDetailsService.loadUserByUsername(username);

                // 1. Validação de Segurança (Versão do Token vs Banco)
                Integer tokenVersion = jwtUtils.getTokenVersion(jwt);
                if (Objects.equals(tokenVersion, userDetails.getTokenVersion())) {

                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);

                    // 2. SLIDING SESSION INTELIGENTE
                    // Verifica quando o token foi criado
                    Date issuedAt = jwtUtils.getIssuedAtDateFromJwtToken(jwt);
                    Instant iat = issuedAt.toInstant();
                    Instant now = Instant.now();

                    // Se o token tem mais de 24 horas de idade...
                    if (iat.isBefore(now.minus(24, ChronoUnit.HOURS))) {

                        // A. Gera novo Cookie/Token (Renova por +7 dias)
                        ResponseCookie newCookie = jwtUtils.generateJwtCookie(userDetails);
                        response.addHeader(HttpHeaders.SET_COOKIE, newCookie.toString());

                        // B. Atualiza métrica de "Visto por último" no banco (Async)
                        updateLastLoginUseCase.execute(userDetails.getId());
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Cannot set user authentication: {}", e);
        }

        filterChain.doFilter(request, response);
    }

    private String parseJwt(HttpServletRequest request) {
        Cookie cookie = WebUtils.getCookie(request, "LV_SESSION");
        return cookie != null ? cookie.getValue() : null;
    }
}