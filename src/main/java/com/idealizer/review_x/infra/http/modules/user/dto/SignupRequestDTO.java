package com.idealizer.review_x.infra.http.modules.user.dto;

import com.idealizer.review_x.domain.user.entities.MobileDeviceType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

@Schema(name = "SignUp")
public record SignupRequestDTO(
        @Size(min = 3, max = 255) String name, @Email @Schema(defaultValue = "user@gmail.com") String email,
        @Size(min = 8) @Schema(defaultValue = "password123") String password,
        @Size(max = 500) @Schema(defaultValue = "\uD83C\uDFAE Story-driven game enthusiast, open-world explorer, and lover of immersive RPGs. Add in epic soundtracks, moral choices, and deep lore — I’m all in. From Night City to Skellige, I’ll take both.") String bio,
        @Schema(defaultValue = "ANDROID") MobileDeviceType mobileDevice
) {
}