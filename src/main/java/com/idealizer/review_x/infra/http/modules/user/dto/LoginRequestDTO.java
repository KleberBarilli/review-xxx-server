package com.idealizer.review_x.infra.http.modules.user.dto;

public record LoginRequestDTO(
    String username,
    String password,
    String locale
) {
}
