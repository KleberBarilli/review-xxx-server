package com.idealizer.review_x.infra.http.controllers.user;

import com.idealizer.review_x.application.user.responses.LoginResponse;
import com.idealizer.review_x.application.user.usecases.FindUserByIdUseCase;
import com.idealizer.review_x.application.user.usecases.SignInUseCase;
import com.idealizer.review_x.common.CommonError;
import com.idealizer.review_x.domain.core.tokens.CookieUtil;
import com.idealizer.review_x.domain.core.tokens.RefreshService;
import com.idealizer.review_x.infra.http.controllers.user.dto.LoginRequestDTO;
import com.idealizer.review_x.security.jwt.JwtUtils;
import com.idealizer.review_x.security.services.UserDetailsImpl;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Value("${auth.refresh.days:60}")
    private int refreshDays;

    @Value("${COOKIE_DOMAIN:}")
    private String cookieDomain;

    private final Logger logger = Logger.getLogger(UserController.class.getName());

    private final SignInUseCase signInUseCase;
    private final FindUserByIdUseCase findUserByIdUseCase;
    private final JwtUtils jwtUtils;
    private final RefreshService refreshService;
    private final CookieUtil cookies;

    private final String COOKIE_DOMAIN = System.getenv("COOKIE_DOMAIN");


    public AuthController(
            SignInUseCase signInUseCase,
            RefreshService refreshService,
            CookieUtil cookies,
            FindUserByIdUseCase findUserByIdUseCase,
            JwtUtils jwtUtils) {
        this.signInUseCase = signInUseCase;
        this.refreshService = refreshService;
        this.cookies = cookies;
        this.findUserByIdUseCase = findUserByIdUseCase;
        this.jwtUtils = jwtUtils;
    }


    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody @Valid LoginRequestDTO dto, HttpServletResponse res) {
        try {
            LoginResponse r = signInUseCase.execute(dto.identifier(), dto.password());
            cookies.setRefreshCookie(res, r.refreshCookieValue(), r.refreshMaxAgeSeconds(), COOKIE_DOMAIN);
            return ResponseEntity.ok(Map.of(
                    "access", r.accessToken(),
                    "user", Map.of("id", r.userId(), "username", r.username(), "roles", r.roles())
            ));
        } catch (AuthenticationException ex) {
            return new ResponseEntity<>(Map.of("code", CommonError.INVALID_CREDENTIALS.code()),
                    CommonError.INVALID_CREDENTIALS.status());
        } catch (Exception e) {
            logger.severe("Signin error: " + e.getMessage());
            return ResponseEntity.status(500).body(Map.of("message", "Signin error"));
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<Map<String, String>> refresh(
            HttpServletRequest req,
            HttpServletResponse res
    ) {
        String raw = cookies.readRefresh(req);
        if (raw == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        var rotatedOpt = refreshService.tryRotateWithReuseDetection(raw, refreshDays);
        if (rotatedOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        var rotated = rotatedOpt.get();
        ObjectId userId = new ObjectId(rotated.stored().userId);
        var user = findUserByIdUseCase.execute(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED));

        var principal = UserDetailsImpl.build(user);
        var access = jwtUtils.generateToken(principal);

        cookies.setRefreshCookie(
                res,
                rotated.cookieValue(),
                refreshDays * 24 * 3600,
                (cookieDomain == null || cookieDomain.isBlank()) ? null : cookieDomain
        );
        return ResponseEntity.ok(java.util.Map.of("access", access));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest req, HttpServletResponse res) {
        getAnyRefreshCookieValue(req).ifPresent(value ->
                refreshService.detectReuseAndRevokeFamilyIfNeeded(value)
        );

        cookies.clearHostRefresh(res);               // apaga __Host-refresh (sem Domain, Secure, Strict, Path=/)
        cookies.clearDevRefresh(res, null);          // apaga refresh (em localhost NÃO use domain; passe null)
        cookies.clearJSessionId(res, false);         // opcional: expira o JSESSIONID (true em prod https)

        return ResponseEntity.noContent().build();
    }

    private Optional<String> getAnyRefreshCookieValue(HttpServletRequest req) {
        Cookie[] cs = req.getCookies();
        if (cs == null) return Optional.empty();
        for (Cookie c : cs) {
            if ("__Host-refresh".equals(c.getName()) || "refresh".equals(c.getName())) {
                return Optional.ofNullable(c.getValue());
            }
        }
        return Optional.empty();
    }

    private Cookie getRefreshCookie(HttpServletRequest req) {
        Cookie[] cs = req.getCookies();
        if (cs == null) return null;
        return Arrays.stream(cs).filter(c -> "__Host-refresh".equals(c.getName())).findFirst().orElse(null);
    }

}