package com.idealizer.review_x.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import com.idealizer.review_x.security.services.UserDetailsImpl;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Component
public class JwtUtils {
    @Value("${auth.jwtSecret}")
    private String jwtSecret;

    @Value("${auth.jwtExpirationDays:7}")
    private int jwtExpirationDays;

    @Value("${auth.cookieSecure:false}")
    private boolean cookieSecure;

    @Value("${JWT_EXPIRATION_IN_MS}")
    private long jwtExpirationMs;

    private SecretKey key;

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    public ResponseCookie generateJwtCookie(UserDetailsImpl userPrincipal) {
        String jwt = generateTokenFromUsername(userPrincipal);
        return generateCookie("LV_SESSION", jwt, Duration.ofDays(jwtExpirationDays));
    }

    public ResponseCookie getCleanJwtCookie() {
        return generateCookie("LV_SESSION", "", Duration.ZERO);
    }

    public String getUserNameFromJwtToken(String token) {
        return Jwts.parser().setSigningKey(key).build()
                .parseClaimsJws(token).getBody().getSubject();
    }

    // Recupera a versão do token para validar se foi revogado
    public Integer getTokenVersion(String token) {
        return (Integer) Jwts.parser().setSigningKey(key).build()
                .parseClaimsJws(token).getBody().get("v");
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(key).build().parse(authToken);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Date getIssuedAtDateFromJwtToken(String token) {
        return Jwts.parser().setSigningKey(key).build()
                .parseClaimsJws(token).getBody().getIssuedAt();
    }
    private String generateTokenFromUsername(UserDetailsImpl user) {
        return Jwts.builder()
                .setSubject(user.getUsername())
                .claim("id", user.getId())
                .claim("v", user.getTokenVersion()) // Importante: Salva a versão
                .setIssuedAt(new Date())
                .setExpiration(Date.from(Instant.now().plus(Duration.ofDays(jwtExpirationDays))))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    private ResponseCookie generateCookie(String name, String value, Duration maxAge) {
        return ResponseCookie.from(name, value)
                .httpOnly(true)
                .secure(cookieSecure)
                .path("/")
                .maxAge(maxAge.getSeconds())
                .sameSite("Lax")
                .build();
    }

    public String generateMobileToken(UserDetailsImpl p, long ttlMillis, Map<String, Object> extraClaims) {
        Instant now = Instant.now();
        Instant exp = now.plusMillis(ttlMillis);

        Map<String, Object> claims = new HashMap<>();
        if (p.getUsername() != null) claims.put("username", p.getUsername());
        if (p.getFullName() != null) claims.put("fullName", p.getFullName());
        if (p.getEmail() != null) claims.put("email", p.getEmail());
        if (p.getAuthorities() != null && !p.getAuthorities().isEmpty()) {
            claims.put("roles", p.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority).toList());
        }
        if (extraClaims != null && !extraClaims.isEmpty()) {
            claims.putAll(extraClaims);
        }

        return Jwts.builder()
                .subject(p.getId().toHexString())
                .issuedAt(Date.from(now))
                .expiration(Date.from(exp))
                .claims(claims)
                .signWith(key())
                .compact();
    }

    public String generateMobileToken(UserDetailsImpl p) {
        Map<String, Object> webClaims = Map.of("aud", "web", "typ", "access");
        return generateMobileToken(p, jwtExpirationMs, webClaims);
    }

    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }
}