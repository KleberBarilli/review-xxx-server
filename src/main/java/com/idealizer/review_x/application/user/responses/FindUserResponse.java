package com.idealizer.review_x.application.user.responses;

import com.idealizer.review_x.application.games.profile.review.responses.LastReviewItemResponse;
import com.idealizer.review_x.domain.profile.game.interfaces.SimpleProfileGame;
import com.idealizer.review_x.domain.user.entities.MobileDeviceType;

import java.util.List;

public class FindUserResponse {
    private String id;
    private String username;
    private String email;
    private String fullName;
    private String bio;
    private String avatarUrl;
    private String steamUrl;
    private String letterboxdUrl;
    private String locale;
    private long followingCount;
    private long followerCount;
    private MobileDeviceType mobileDevice;
    private List<SimpleProfileGame> favoriteGames;
    private List<LastReviewItemResponse> lastReviews;


    public String getLetterboxdUrl() {
        return letterboxdUrl;
    }

    public void setLetterboxdUrl(String letterboxdUrl) {
        this.letterboxdUrl = letterboxdUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getSteamUrl() {
        return steamUrl;
    }

    public void setSteamUrl(String steamUrl) {
        this.steamUrl = steamUrl;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public MobileDeviceType getMobileDevice() {
        return mobileDevice;
    }

    public void setMobileDevice(MobileDeviceType mobileDevice) {
        this.mobileDevice = mobileDevice;
    }

    public List<SimpleProfileGame> getFavoriteGames() {
        return favoriteGames;
    }

    public void setFavoriteGames(List<SimpleProfileGame> favoriteGames) {
        this.favoriteGames = favoriteGames;
    }
    public List<LastReviewItemResponse> getLastReviews() {
        return lastReviews;
    }
    public void setLastReviews(List<LastReviewItemResponse> lastReviews) {
        this.lastReviews = lastReviews;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public long getFollowingCount() {
        return followingCount;
    }

    public void setFollowingCount(long followingCount) {
        this.followingCount = followingCount;
    }

    public long getFollowerCount() {
        return followerCount;
    }

    public void setFollowerCount(long followerCount) {
        this.followerCount = followerCount;
    }
}
