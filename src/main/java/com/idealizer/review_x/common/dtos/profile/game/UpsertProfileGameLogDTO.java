package com.idealizer.review_x.common.dtos.profile.game;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import net.minidev.json.annotate.JsonIgnore;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;


public record UpsertProfileGameLogDTO(
        String profileGameId,
        @Min(1)
        @Max(31)
        int day,
        @Min(1)
        @Max(12)
        int month,
        @Min(1970)
        int year,
        @Schema(example = "90")
        @PositiveOrZero
        int minutesPlayed,
        @Size(max = 500)
        String note

        ) {

    private static final ZoneId CANONICAL_ZONE = ZoneOffset.UTC;

    private static final int ALLOWED_FUTURE_DAYS = 1;

    @AssertTrue(message = "Invalid date")
    @Schema(hidden = true)
    @JsonIgnore
    public boolean isValidDateWithLeniency() {
        try {
            LocalDate date = LocalDate.of(year, month, day);

            LocalDate todayUtc = LocalDate.now(CANONICAL_ZONE);

            return !date.isAfter(todayUtc.plusDays(ALLOWED_FUTURE_DAYS));
        } catch (DateTimeException e) {
            return false;
        }
    }
}
