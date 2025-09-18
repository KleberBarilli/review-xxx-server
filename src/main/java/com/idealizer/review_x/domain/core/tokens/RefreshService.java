package com.idealizer.review_x.domain.core.tokens;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.util.Base64;
import java.util.Optional;

@Service
public class RefreshService {
    private final RefreshTokenRepo repo;
    private final BCryptPasswordEncoder enc = new BCryptPasswordEncoder(10);
    private final SecureRandom rnd = new SecureRandom();

    public RefreshService(RefreshTokenRepo repo) {
        this.repo = repo;
    }

    public record Pair(String cookieValue, RefreshToken stored) {}

    private String randomId(int bytes) {
        byte[] b = new byte[bytes];
        rnd.nextBytes(b);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(b);
    }

    public Pair issue(String userId, String familyId, long daysTtl) {
        String jti = randomId(16);
        String value = randomId(32);

        RefreshToken rt = new RefreshToken();
        rt.jti = jti;
        rt.familyId = (familyId != null) ? familyId : randomId(12);
        rt.userId = userId;
        rt.hash = enc.encode(value);
        rt.expiresAt = Instant.now().plus(Duration.ofDays(daysTtl));
        rt.ttl = Duration.ofDays(daysTtl).toSeconds();

        repo.save(rt);
        return new Pair(jti + "." + value, rt);
    }

    public Optional<RefreshToken> validateCookie(String cookieValue) {
        String[] parts = split(cookieValue);
        if (parts == null) return Optional.empty();
        String jti = parts[0], value = parts[1];

        return repo.findById(jti).flatMap(rt -> {
            boolean valid = !rt.revoked
                    && rt.expiresAt != null
                    && rt.expiresAt.isAfter(Instant.now())
                    && enc.matches(value, rt.hash);
            return valid ? Optional.of(rt) : Optional.empty();
        });
    }

    public boolean detectReuseAndRevokeFamilyIfNeeded(String cookieValue) {
        String[] parts = split(cookieValue);
        String presented = (parts != null && parts.length == 2) ? parts[1] : cookieValue;

        for (RefreshToken r : repo.findAll()) {
            if (enc.matches(presented, r.hash)) {
                revokeFamily(r.familyId);
                return true;
            }
        }
        return false;
    }

    public void revokeFamily(String familyId) {
        for (RefreshToken r : repo.findAll()) {
            if (familyId.equals(r.familyId) && !r.revoked) {
                r.revoked = true;
                repo.save(r);
            }
        }
    }

    public Pair rotate(RefreshToken oldRt, long daysTtl) {
        String familyId = oldRt.familyId;
        String userId = oldRt.userId;

        Pair newPair = issue(userId, familyId, daysTtl);
        oldRt.revoked = true;
        oldRt.replacedBy = newPair.stored().jti;
        repo.save(oldRt);
        return newPair;
    }

    public Optional<Pair> rotate(String rawCookie, long daysTtl) {
        return validateCookie(rawCookie).map(rt -> rotate(rt, daysTtl));
    }
    public Optional<Pair> tryRotateWithReuseDetection(String rawCookie, long daysTtl) {
        var opt = validateCookie(rawCookie);
        if (opt.isEmpty()) {
            detectReuseAndRevokeFamilyIfNeeded(rawCookie);
            return Optional.empty();
        }
        return Optional.of(rotate(opt.get(), daysTtl));
    }

    private String[] split(String cookieValue) {
        if (cookieValue == null) return null;
        int idx = cookieValue.indexOf('.');
        if (idx <= 0 || idx >= cookieValue.length() - 1) return null;
        return new String[]{ cookieValue.substring(0, idx), cookieValue.substring(idx + 1) };
    }
}
