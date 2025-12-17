package com.idealizer.review_x.security.jwt;

import com.idealizer.review_x.application.user.usecases.UpdateLastLoginUseCase;
import com.idealizer.review_x.security.services.UserDetailsImpl;
import com.idealizer.review_x.security.services.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;

import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Objects;

public class JwtAuthTokenFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthTokenFilter.class);

    private final JwtUtils jwtUtils;
    private final UserDetailsServiceImpl userDetailsService;
    private final UpdateLastLoginUseCase updateLastLoginUseCase;

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
            String jwt = null;
            boolean isCookieSession = false;

            Cookie cookie = WebUtils.getCookie(request, "LV_SESSION");
            if (cookie != null) {
                jwt = cookie.getValue();
                isCookieSession = true;
                logger.debug("Token JWT encontrado no cookie LV_SESSION.");
            }
            else {
                String headerAuth = request.getHeader("Authorization");
                if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
                    jwt = headerAuth.substring(7);
                    isCookieSession = false;
                    logger.debug("Token JWT encontrado no header Authorization.");
                }
            }

            logger.debug("Token JWT encontrado com username {}.", jwt);
            logger.debug("Token é valido? {}.", jwtUtils.validateJwtToken(jwt));
            if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
                String username = jwtUtils.getUserNameFromJwtToken(jwt);
                logger.debug("Token JWT encontrado com username {}.", username);
                UserDetailsImpl userDetails = (UserDetailsImpl) userDetailsService.loadUserByUsername(username);

                Integer tokenVersion = jwtUtils.getTokenVersion(jwt);
                if (Objects.equals(tokenVersion, userDetails.getTokenVersion())) {

                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authentication);

                    if (isCookieSession) {
                        Date issuedAt = jwtUtils.getIssuedAtDateFromJwtToken(jwt);
                        Instant iat = issuedAt.toInstant();
                        Instant now = Instant.now();

                        if (iat.isBefore(now.minus(24, ChronoUnit.HOURS))) {
                            ResponseCookie newCookie = jwtUtils.generateJwtCookie(userDetails);
                            response.addHeader(HttpHeaders.SET_COOKIE, newCookie.toString());

                            updateLastLoginUseCase.execute(userDetails.getId());
                            logger.info("Sliding Session: Cookie renovado para o usuário: {}", username);
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Não foi possível definir a autenticação do usuário: {}", e.getMessage());
        }

        filterChain.doFilter(request, response);
    }
}