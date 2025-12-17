package com.idealizer.review_x.infra.http.controllers.user;

import com.idealizer.review_x.application.user.usecases.FindUserByIdUseCase;
import com.idealizer.review_x.application.user.usecases.SignInUseCase;
import com.idealizer.review_x.application.user.usecases.UpdateLastLoginUseCase;
import com.idealizer.review_x.infra.http.controllers.user.dto.LoginRequestDTO;
import com.idealizer.review_x.security.jwt.JwtUtils;
import com.idealizer.review_x.security.services.UserDetailsImpl;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    @Value("${auth.refresh.days:60}")
    private int refreshDays;

    @Value("${COOKIE_DOMAIN:}")
    private String cookieDomain;

    private final Logger logger = Logger.getLogger(UserController.class.getName());

    private final SignInUseCase signInUseCase;
    private final FindUserByIdUseCase findUserByIdUseCase;
    private final UpdateLastLoginUseCase updateLastLoginUseCase;
    private final JwtUtils jwtUtils;

    private final String COOKIE_DOMAIN = System.getenv("COOKIE_DOMAIN");


    public AuthController(
            SignInUseCase signInUseCase,
            FindUserByIdUseCase findUserByIdUseCase,
            UpdateLastLoginUseCase updateLastLoginUseCase,
            JwtUtils jwtUtils, AuthenticationManager authenticationManager) {
        this.signInUseCase = signInUseCase;
        this.findUserByIdUseCase = findUserByIdUseCase;
        this.updateLastLoginUseCase = updateLastLoginUseCase;
        this.jwtUtils = jwtUtils;
        this.authenticationManager = authenticationManager;
    }


    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody @Valid LoginRequestDTO dto, HttpServletResponse res) {
        UserDetailsImpl userDetails = signInUseCase.execute(dto.identifier(), dto.password());
        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);
        res.addHeader(HttpHeaders.SET_COOKIE, jwtCookie.toString());
        updateLastLoginUseCase.execute(userDetails.getId());
        return ResponseEntity.ok(Map.of(
                "user", Map.of(
                        "id", userDetails.getId().toHexString(),
                        "username", userDetails.getUsername()
                )
        ));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser(HttpServletResponse res) {
        ResponseCookie cookie = jwtUtils.getCleanJwtCookie();
        res.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        return ResponseEntity.ok().build();
    }
}