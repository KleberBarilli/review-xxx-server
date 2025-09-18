package com.idealizer.review_x.config;

import com.idealizer.review_x.security.jwt.AuthEntryPointJwt;
import com.idealizer.review_x.security.jwt.JwtAuthTokenFilter;
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
    private final JwtAuthTokenFilter jwtAuthTokenFilter;
    private final @Lazy OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
    private final @Lazy OAuth2LoginFailureHandler oAuth2LoginFailureHandler;
    private final RefreshOriginFilter refreshOriginFilter;

    public SecurityConfiguration (AuthEntryPointJwt unauthorizedHandler,
                                  JwtAuthTokenFilter jwtAuthTokenFilter,
                                  OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler,
                                  OAuth2LoginFailureHandler oAuth2LoginFailureHandler,
                                  RefreshOriginFilter refreshOriginFilter
                                  ){
        this.unauthorizedHandler = unauthorizedHandler;
        this.jwtAuthTokenFilter = jwtAuthTokenFilter;
        this.oAuth2LoginSuccessHandler = oAuth2LoginSuccessHandler;
        this.oAuth2LoginFailureHandler = oAuth2LoginFailureHandler;
        this.refreshOriginFilter = refreshOriginFilter;

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
                                "/api/auth/refresh",
                                "/api/csrf-token"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2Login(o -> o
                        .successHandler(oAuth2LoginSuccessHandler)
                        .failureHandler(oAuth2LoginFailureHandler)
                );
        http.addFilterBefore(refreshOriginFilter, UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(jwtAuthTokenFilter, UsernamePasswordAuthenticationFilter.class);
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
