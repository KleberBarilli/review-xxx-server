package com.idealizer.review_x.infra.http.controllers.user.dto;

import com.idealizer.review_x.domain.user.entities.MobileDeviceType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

import java.text.Normalizer;
import java.util.Locale;

@Schema(name = "SignUp")
public record SignupRequestDTO(
        @Size(min = 3, max = 255)
        String name,
        @Email @Schema(defaultValue = "user@gmail.com")
        String email,
        @Size(min = 3, max = 255)
        @Schema(defaultValue = "John Doe")
        String fullName,
        @Size(min = 8)
        @Schema(defaultValue = "password123")
        String password,
        @Size(max = 500)
        @Schema(defaultValue = "\uD83C\uDFAE Story-driven game enthusiast, open-world explorer," +
                " and lover of immersive RPGs. Add in epic soundtracks, moral choices, and deep lore" +
                " — I’m all in. From Night City to Skellige, I’ll take both.")
        String bio,
        @Schema(defaultValue = "ANDROID") MobileDeviceType mobileDevice
) {
        public SignupRequestDTO {
                if (name != null) {
                        name = normalizeUsername(name);
                }
                if (email != null) {
                        email = email.trim().toLowerCase(Locale.ROOT);
                }
                if (fullName != null) {
                        fullName = fullName.trim();
                }
        }

        private static String normalizeUsername(String s) {
                String out = s.trim().toLowerCase(Locale.ROOT);

                out = Normalizer.normalize(out, Normalizer.Form.NFD)
                        .replaceAll("\\p{M}", "");

                out = out.replaceAll("[^a-z0-9._-]", "");

                out = out.replaceAll("[._-]{2,}", "-")
                        .replaceAll("^[._-]+|[._-]+$", "");

                return out;
        }
}