package com.idealizer.review_x.common.dtos.activity.follow;

import com.idealizer.review_x.domain.core.user.entities.User;

import java.time.Instant;

public class FollowerResponse {
    private String id;
    private String name;
    private String fullName;
    private String avatarUrl;
    private String bio;
    private Instant followedAt;
    private boolean isFollowing;
    private boolean isSameUser;

    public FollowerResponse(User user, Instant followedAt, boolean isFollowing, boolean isSameUser) {
        this.id = user.getId().toString();
        this.name = user.getName();
        this.fullName = user.getFullName();
        this.avatarUrl = user.getAvatarUrl();
        this.bio = user.getBio();
        this.followedAt = followedAt;
        this.isFollowing = isFollowing;
        this.isSameUser = isSameUser;
    }

    // Getters
    public String getId() { return id; }
    public String getName() { return name; }
    public String getFullName() { return fullName; }
    public String getAvatarUrl() { return avatarUrl; }
    public String getBio() { return bio; }
    public Instant getFollowedAt() { return followedAt; }
    public boolean getIsFollowing() { return isFollowing; }
    public boolean getIsSameUser(){return isSameUser;}
}