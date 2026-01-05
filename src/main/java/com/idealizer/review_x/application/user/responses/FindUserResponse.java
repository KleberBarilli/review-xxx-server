package com.idealizer.review_x.application.user.responses;

import com.idealizer.review_x.application.review.responses.LastReviewItemResponse;
import com.idealizer.review_x.domain.core.profile.game.interfaces.SimpleProfileGame;
import com.idealizer.review_x.domain.core.user.entities.MobileDeviceType;

import java.util.List;

public class FindUserResponse {
    private String id;
    private String username;
    private String email;
    private String fullName;
    private String bio;
    private String avatarUrl;
    private String locale;
    private long followingCount;
    private long followerCount;
    private MobileDeviceType mobileDevice;
    private List<SimpleProfileGame> favoriteGames;
    private List<LastReviewItemResponse> lastReviews;
    private List<SimpleProfileGame> masteredGames;
    private List<SimpleProfileGame> playingGames;

    //social links
    private String steamUrl;
    private String psnUrl;
    private String xboxUrl;
    private String letterboxdUrl;
    private String instagramUrl;
    private String twitterUrl;
    private String redditUrl;
    private String youtubeUrl;
    private String discordUrl;

    //utils
    private boolean isFollowing;
    private boolean isMe;






    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setIsFollowing(boolean isFollowing) {
        this.isFollowing = isFollowing;
    }

    public boolean isFollowing() {
        return isFollowing;
    }

    public boolean isMe() {
        return isMe;
    }

    public void setIsMe(boolean isMe) {
        this.isMe = isMe;
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

    public List<SimpleProfileGame> getMasteredGames() {
        return masteredGames;
    }

    public void setMasteredGames(List<SimpleProfileGame> masteredGames) {
        this.masteredGames = masteredGames;
    }

    public List<SimpleProfileGame> getPlayingGames() {
        return playingGames;
    }

    public void setPlayingGames(List<SimpleProfileGame> playingGames) {
        this.playingGames = playingGames;
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

    public String getLetterboxdUrl() {
        return letterboxdUrl;
    }

    public void setLetterboxdUrl(String letterboxdUrl) {
        this.letterboxdUrl = letterboxdUrl;
    }

    public String getSteamUrl() {
        return steamUrl;
    }

    public void setSteamUrl(String steamUrl) {
        this.steamUrl = steamUrl;
    }

    public String getPsnUrl() {
        return psnUrl;
    }

    public void setPsnUrl(String psnUrl) {
        this.psnUrl = psnUrl;
    }

    public String getXboxUrl() {
        return xboxUrl;
    }

    public void setXboxUrl(String xboxUrl) {
        this.xboxUrl = xboxUrl;
    }

    public String getInstagramUrl() {
        return instagramUrl;
    }

    public void setInstagramUrl(String instagramUrl) {
        this.instagramUrl = instagramUrl;
    }

    public String getTwitterUrl() {
        return twitterUrl;
    }

    public void setTwitterUrl(String twitterUrl) {
        this.twitterUrl = twitterUrl;
    }

    public String getRedditUrl() {
        return redditUrl;
    }

    public void setRedditUrl(String redditUrl) {
        this.redditUrl = redditUrl;
    }

    public String getYoutubeUrl() {
        return youtubeUrl;
    }

    public void setYoutubeUrl(String youtubeUrl) {
        this.youtubeUrl = youtubeUrl;
    }

    public String getDiscordUrl() {
        return discordUrl;
    }

    public void setDiscordUrl(String discordUrl) {
        this.discordUrl = discordUrl;
    }
}
