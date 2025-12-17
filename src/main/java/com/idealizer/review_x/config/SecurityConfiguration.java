package com.idealizer.review_x.config;

import com.idealizer.review_x.application.user.usecases.UpdateLastLoginUseCase;
import com.idealizer.review_x.security.jwt.AuthEntryPointJwt;
import com.idealizer.review_x.security.jwt.JwtAuthTokenFilter;
import com.idealizer.review_x.security.jwt.JwtUtils;
import com.idealizer.review_x.security.services.UserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    private final AuthEntryPointJwt unauthorizedHandler;

    private final JwtUtils jwtUtils;
    private final UserDetailsServiceImpl userDetailsService;
    private final UpdateLastLoginUseCase updateLastLoginUseCase;

    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
    private final OAuth2LoginFailureHandler oAuth2LoginFailureHandler;

    public SecurityConfiguration(AuthEntryPointJwt unauthorizedHandler,
                                 JwtUtils jwtUtils,
                                 UserDetailsServiceImpl userDetailsService,
                                 UpdateLastLoginUseCase updateLastLoginUseCase,
                                 @Lazy OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler,
                                 @Lazy OAuth2LoginFailureHandler oAuth2LoginFailureHandler) {
        this.unauthorizedHandler = unauthorizedHandler;
        this.jwtUtils = jwtUtils;
        this.userDetailsService = userDetailsService;
        this.updateLastLoginUseCase = updateLastLoginUseCase;
        this.oAuth2LoginSuccessHandler = oAuth2LoginSuccessHandler;
        this.oAuth2LoginFailureHandler = oAuth2LoginFailureHandler;
    }

    // Criamos o Bean do filtro manualmente, passando as dependências
    @Bean
    public JwtAuthTokenFilter authenticationJwtTokenFilter() {
        return new JwtAuthTokenFilter(jwtUtils, userDetailsService, updateLastLoginUseCase);
    }

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(csrf -> csrf.disable())
                .cors(Customizer.withDefaults())
                .exceptionHandling(ex -> ex.authenticationEntryPoint(unauthorizedHandler))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/public/**",
                                "/api/users/public/**",
                                "/api/profile-games/public/**",
                                "/api/reviews/public/**"
                        ).permitAll()
                        .requestMatchers(
                                "/api/auth/signin",
                                "/api/users/signup",
                                "/api/users/mobile/signin",
                                "/api/auth/logout",
                                "/api/csrf-token"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2Login(o -> o
                        .successHandler(oAuth2LoginSuccessHandler)
                        .failureHandler(oAuth2LoginFailureHandler)
                );

        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring().requestMatchers(
                "/v2/api-docs",
                "/v3/api-docs/**",
                "/swagger-resources/**",
                "/swagger-ui/**",
                "/webjars/**",
                "/actuator/**"
        );
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        var cfg = new CorsConfiguration();
        cfg.setAllowedOrigins(List.of(
                "http://localhost:3000",
                "https://loggvault.com"
        ));
        cfg.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        cfg.setAllowedHeaders(List.of("Content-Type", "Authorization", "X-XSRF-TOKEN"));
        cfg.setAllowCredentials(true);
        var src = new UrlBasedCorsConfigurationSource();
        src.registerCorsConfiguration("/**", cfg);
        return src;
    }
}