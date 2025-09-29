package com.idealizer.review_x.domain.core.tokens;

import com.mongodb.lang.Nullable;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

@Component
public class CookieUtil {
    public void setRefreshCookie(HttpServletResponse res, String value, int maxAgeSec, String domain) {
        Cookie c = new Cookie("__Host-refresh", value);
        c.setHttpOnly(true);
        c.setSecure(true);
        c.setPath("/");
        if (domain != null && !domain.isBlank()) c.setDomain(domain);
        c.setMaxAge(maxAgeSec);
        c.setAttribute("SameSite","Strict");
        res.addCookie(c);
    }
    public void clearHostRefresh(HttpServletResponse res) {
        ResponseCookie c = ResponseCookie.from("__Host-refresh", "")
                .httpOnly(true)
                .secure(true)
                .sameSite("Strict")
                .path("/")
                .maxAge(0)
                .build();
        res.addHeader(HttpHeaders.SET_COOKIE, c.toString());
    }

    public void clearDevRefresh(HttpServletResponse res, @Nullable String domain) {
        ResponseCookie.ResponseCookieBuilder b = ResponseCookie.from("refresh", "")
                .httpOnly(true)
                .secure(false)
                .sameSite("Lax")
                .path("/")
                .maxAge(0);
        if (domain != null && !domain.isBlank()) {
            b.domain(domain);
        }
        res.addHeader(HttpHeaders.SET_COOKIE, b.build().toString());
    }

    public void clearJSessionId(HttpServletResponse res, boolean secure) {
        ResponseCookie c = ResponseCookie.from("JSESSIONID", "")
                .httpOnly(true)
                .secure(secure)
                .sameSite(secure ? "Strict" : "Lax")
                .path("/")
                .maxAge(0)
                .build();
        res.addHeader(HttpHeaders.SET_COOKIE, c.toString());
    }

    @Nullable
    public String readRefresh(HttpServletRequest req) {
        return readCookie(req, "__Host-refresh", "refresh");
    }

    @Nullable
    private String readCookie(HttpServletRequest req, String... names) {
        Cookie[] cs = req.getCookies();
        if (cs != null) {
            for (String name : names) {
                for (Cookie c : cs) {
                    if (name.equals(c.getName())) {
                        String v = normalizeCookieValue(c.getValue());
                        if (v != null) return v;
                    }
                }
            }
        }
        String header = req.getHeader("Cookie");
        if (header != null) {
            String[] parts = header.split(";");
            for (String part : parts) {
                int idx = part.indexOf('=');
                if (idx > 0) {
                    String n = part.substring(0, idx).trim();
                    String v = normalizeCookieValue(part.substring(idx + 1).trim());
                    if (v != null) {
                        for (String name : names) {
                            if (name.equals(n)) return v;
                        }
                    }
                }
            }
        }

        return null;
    }

    @Nullable
    private String normalizeCookieValue(@Nullable String v) {
        if (v == null) return null;
        v = v.trim();
        if (v.length() >= 2 && v.startsWith("\"") && v.endsWith("\"")) {
            v = v.substring(1, v.length() - 1);
        }
        return v.isEmpty() ? null : v;
    }
}
