package com.idealizer.review_x.application.modules.users.controllers.dto;

import com.idealizer.review_x.application.modules.users.entities.MobileDeviceType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.URL;

@Schema(name = "SocialLogin")
public record SocialLoginDTO(
        String idToken , @Size(min = 3, max = 255)  String name, @Email String email , @Size(max=500) String bio,
        @URL(message = "Invalid URL format") @Schema(defaultValue = "https://mir-s3-cdn-cf.behance.net/projects/404/d9a9f2168304619.Y3JvcCwzMTc0LDI0ODMsMTY1LDA.jpg") String avatarUrl,
        @URL(message = "Invalid URL format") @Schema(defaultValue = "https://letterboxd.com/user") String letterboxdUrl,
        @URL(message = "Invalid URL format") @Schema(defaultValue = "https://steamcommunity.com/id/user/") String steamUrl,
        @Schema(defaultValue = "ANDROID") MobileDeviceType mobileDevice, @Schema(defaultValue = "pt_BR") String locale
        ) {
}