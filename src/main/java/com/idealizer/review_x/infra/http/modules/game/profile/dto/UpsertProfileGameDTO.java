package com.idealizer.review_x.infra.http.modules.game.profile.dto;

import com.idealizer.review_x.common.MessageUtil;
import com.idealizer.review_x.common.validators.ValidUrl;
import com.idealizer.review_x.domain.game.entities.GamePlatform;
import com.idealizer.review_x.domain.profile.game.PlatformType;
import com.idealizer.review_x.domain.profile.game.ProfileGameStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

import java.util.List;

public class UpsertProfileGameDTO {

    private final MessageUtil messageUtil;

    @Schema(defaultValue = "1")
    private Long matchedGameId;

    @Schema(defaultValue = "PC_MICROSOFT_WINDOWS")
    private GamePlatform playedOn;

    @Schema(defaultValue = "STEAM")
    private PlatformType sourcePlatform;

    @Schema(defaultValue = "COMPLETED")
    private ProfileGameStatus status;

    @Schema(defaultValue = "1000")
    @Min(1)
    @Max(3000000)
    private Integer playtimeInMinutes;

    @Schema(defaultValue = "false")
    private Boolean playing;

    @Schema(defaultValue = "false")
    private Boolean favorite;

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


    private UpsertReviewGameDTO review;


    public UpsertProfileGameDTO(MessageUtil messageUtil, Long matchedGameId, GamePlatform playedOn, PlatformType sourcePlatform,
                                ProfileGameStatus status, Integer playtimeInMinutes, Boolean playing,
                                Boolean favorite, Boolean owned, Boolean wishlist, Boolean mastered,
                                Integer rating, List<String> favoriteScreenshots, UpsertReviewGameDTO review) {
        this.messageUtil = messageUtil;
        this.matchedGameId = matchedGameId;
        this.playedOn = playedOn;
        this.sourcePlatform = sourcePlatform;
        this.status = status;
        this.playtimeInMinutes = playtimeInMinutes;
        this.playing = playing;
        this.favorite = favorite;
        this.owned = owned;
        this.wishlist = wishlist;
        this.mastered = mastered;
        this.rating = rating;
        this.favoriteScreenshots = favoriteScreenshots;
        this.review = review;
    }

    public Long getMatchedGameId() {
        return matchedGameId;
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

    public Integer getPlaytimeInMinutes() {
        return playtimeInMinutes;
    }

    public Boolean getPlaying() {
        return playing;
    }

    public Boolean getFavorite() {
        return favorite;
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

    public UpsertReviewGameDTO getReview() {
        return review;
    }
}
