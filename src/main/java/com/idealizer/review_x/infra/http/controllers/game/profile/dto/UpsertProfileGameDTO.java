package com.idealizer.review_x.infra.http.controllers.game.profile.dto;

import com.idealizer.review_x.common.validators.ValidUrl;
import com.idealizer.review_x.domain.game.entities.enums.GamePlatform;
import com.idealizer.review_x.domain.core.profile.game.entities.PlatformType;
import com.idealizer.review_x.domain.core.profile.game.entities.ProfileGameStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;

import java.time.Instant;
import java.util.List;

public class UpsertProfileGameDTO {

    @Schema(defaultValue = "PC_MICROSOFT_WINDOWS")
    private GamePlatform playedOn;

    @Schema(defaultValue = "STEAM")
    private PlatformType sourcePlatform;

    @Schema(defaultValue = "COMPLETED")
    private ProfileGameStatus status;


    @Schema(defaultValue = "false")
    private Boolean playing;

    @Schema(defaultValue = "false")
    private Boolean liked;

    @Schema(defaultValue = "true")
    private Boolean owned;

    @Schema(defaultValue = "false")
    private Boolean wishlist;

    @Schema(defaultValue = "true")
    private Boolean mastered;

    @Schema(defaultValue = "9")
    @Min(1)
    @Max(10)
    private Integer rating;

    @Size(max = 10)
    @ValidUrl
    private List<String> favoriteScreenshots;

    @Schema(defaultValue = "2025-07-25T12:00:00Z")
    @PastOrPresent(message = "The start date cannot be in the future")
    private Instant startedAt;

    @Schema(defaultValue = "2025-07-30T12:00:00Z")
    @PastOrPresent(message = "The finish date cannot be in the future")
    private Instant finishedAt;

    @Schema(defaultValue = "GOTY 2025")
    @Size(min = 3, max = 50, message = "The title must be between 3 and 50 characters")
    private String reviewTitle;

    @Schema(defaultValue = "This is a sample review content. Share your thoughts about the game, what you liked," +
            "what could be improved, and your overall experience. Be honest and constructive to help other players" +
            " make informed decisions!")
    @Size(min = 3, max = 500, message = "The content must be between 3 and 500 characters")
    private String reviewContent;

    @Schema(defaultValue = "false")
    private Boolean reviewSpoiler;

    @Schema(defaultValue = "false")
    private Boolean reviewReplay;

    public UpsertProfileGameDTO(GamePlatform playedOn, PlatformType sourcePlatform, ProfileGameStatus status
                                , Boolean playing, Boolean liked, Boolean owned,
                                Boolean wishlist, Boolean mastered, Integer rating, List<String> favoriteScreenshots, String reviewTitle,
                                String reviewContent, Boolean reviewSpoiler, Boolean reviewReplay,
                                Instant startedAt, Instant finishedAt) {
        this.playedOn = playedOn;
        this.sourcePlatform = sourcePlatform;
        this.status = status;
        this.playing = playing;
        this.liked = liked;
        this.owned = owned;
        this.wishlist = wishlist;
        this.mastered = mastered;
        this.rating = rating;
        this.favoriteScreenshots = favoriteScreenshots;
        this.startedAt = startedAt;
        this.finishedAt = finishedAt;
        this.reviewTitle = reviewTitle;
        this.reviewContent = reviewContent;
        this.reviewSpoiler = reviewSpoiler;
        this.reviewReplay = reviewReplay;
    }

    public GamePlatform getPlayedOn() {
        return playedOn;
    }

    public PlatformType getSourcePlatform() {
        return sourcePlatform;
    }

    public ProfileGameStatus getStatus() {
        return status;
    }


    public Boolean getPlaying() {
        return playing;
    }

    public Boolean getLiked() {
        return liked;
    }

    public Boolean getOwned() {
        return owned;
    }

    public Boolean getWishlist() {
        return wishlist;
    }

    public Boolean getMastered() {
        return mastered;
    }

    public Integer getRating() {
        return rating;
    }

    public List<String> getFavoriteScreenshots() {
        return favoriteScreenshots;
    }

    public Instant getStartedAt() {
        return startedAt;
    }
    public Instant getFinishedAt() {
        return finishedAt;
    }

    public String getReviewTitle() {
        return reviewTitle;
    }

    public String getReviewContent() {
        return reviewContent;
    }

    public Boolean getReviewSpoiler() {
        return reviewSpoiler;
    }

    public Boolean getReviewReplay() {
        return reviewReplay;
    }


}
