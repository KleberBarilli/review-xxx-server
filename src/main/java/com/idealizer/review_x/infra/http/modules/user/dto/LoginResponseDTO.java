package com.idealizer.review_x.infra.http.modules.user.dto;

import java.util.List;

public record LoginResponseDTO(
        String token,
        String username,
        List<String> roles
) {
}
