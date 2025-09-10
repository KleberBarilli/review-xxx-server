package com.idealizer.review_x.domain.core.user.entities;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;
import java.util.Objects;

@Document(collection = "users")
public class User {

    @Id
    private ObjectId id;

    @Indexed(unique = true)
    private String name;
    @Indexed(unique = true)
    private String email;
    private String password;
    @Field(value = "full_name")
    private String fullName;
    private String bio;
    @Field(value = "avatar_url")
    private String avatarUrl;
    @Field(value = "letterboxd_url")
    private String letterboxdUrl;
    @Field(value = "steam_url")
    private String steamUrl;
    @Field(value = "psn_url")
    private String psnUrl;
    @Field(value = "xbox_url")
    private String xboxUrl;
    private String locale;
    @Field(value = "mobile_device")
    private MobileDeviceType mobileDevice;
    private String signUpMethod;

    @Field(value = "follower_count")
    private long followerCount = 0L;
    @Field(value = "following_count")
    private long followingCount = 0L;

    @Field(value = "created_at")
    @CreatedDate
    private Instant createdAt;

    @Field(value = "updated_at")
    @LastModifiedDate
    private Instant updatedAt;

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSignUpMethod() {
        return signUpMethod;
    }

    public void setSignUpMethod(String signUpMethod) {
        this.signUpMethod = signUpMethod;
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

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public long getFollowerCount() {
        return followerCount;
    }

    public void setFollowerCount(long followerCount) {
        this.followerCount = followerCount;
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

    public long getFollowingCount() {
        return followingCount;
    }

    public void setFollowingCount(long followingCount) {
        this.followingCount = followingCount;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass())
            return false;
        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(name, user.name) && Objects.equals(email, user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, email);
    }
}
