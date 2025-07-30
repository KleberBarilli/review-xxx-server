package com.idealizer.review_x.infra.http.modules.game.profile.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;

import java.time.Instant;

public class UpsertReviewGameDTO {
    @Schema(defaultValue = "GOTY 2025")
    @Size(min = 3, max = 50, message = "The title must be between 3 and 50 characters")
    private String title;

    @Schema(defaultValue = "This is a sample review content. Share your thoughts about the game, what you liked," +
            "what could be improved, and your overall experience. Be honest and constructive to help other players" +
            " make informed decisions!")
    @Size(min = 3, max = 500, message = "The content must be between 3 and 500 characters")
    private String content;

    @Schema(defaultValue = "false")
    private Boolean spoiler;

    @Schema(defaultValue = "false")
    private Boolean replay;

    @Schema(defaultValue = "2025-07-25T12:00:00Z")
    @PastOrPresent(message = "The start date cannot be in the future")
    private Instant startedAt;

    @Schema(defaultValue = "2025-07-30T12:00:00Z")
    @PastOrPresent(message = "The finish date cannot be in the future")
    private Instant finishedAt;

    public UpsertReviewGameDTO() {}

    public UpsertReviewGameDTO(String title, String content, Boolean spoiler, Boolean replay, Instant startedAt, Instant finishedAt) {
        this.title = title;
        this.content = content;
        this.spoiler = spoiler;
        this.replay = replay;
        this.startedAt = startedAt;
        this.finishedAt = finishedAt;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public Boolean getSpoiler() {
        return spoiler;
    }

    public Boolean getReplay() {
        return replay;
    }

    public Instant getStartedAt() {
        return startedAt;
    }

    public Instant getFinishedAt() {
        return finishedAt;
    }
}
