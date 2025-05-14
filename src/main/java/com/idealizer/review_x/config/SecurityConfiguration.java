package com.idealizer.review_x.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                //.httpBasic(Customizer.withDefaults())
                //.formLogin(Customizer.withDefaults())
                .authorizeHttpRequests(authorize ->{
                    authorize.requestMatchers("/login/**").permitAll();
                    authorize.anyRequest().authenticated();
                })
                .oauth2Login(Customizer.withDefaults())
                .build();
    }

}
