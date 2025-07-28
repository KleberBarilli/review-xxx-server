package com.idealizer.review_x.infra.http.modules.user.dto;

import com.idealizer.review_x.domain.user.entities.MobileDeviceType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

@Schema(name = "SignUp")
public record SignupRequestDTO(
        @Size(min = 3, max = 255)  String name, @Email String email , @Size(min = 8)  String password , @Size(max=500) String bio,
        @Schema(defaultValue = "ANDROID") MobileDeviceType mobileDevice, @Schema(defaultValue = "pt_BR") String locale
        ) {
}