package com.idealizer.review_x.modules.users.entities;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Objects;

@Document(collection = "users")
public class User {

    @Id
    private ObjectId id;

    private String name;
    @Indexed(unique = true)
    private String email;
    private String bio;
    private String avatarUrl;
    private String letterboxdUrl;
    private String steamUrl;
    private String locale;
    private MobileDeviceType mobileDevice;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

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

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass())
            return false;
        User user = (User) o;
        return Objects.equals(id, user.id)
                && Objects.equals(name, user.name)
                && Objects.equals(bio, user.bio)
                && Objects.equals(avatarUrl, user.avatarUrl)
                && Objects.equals(email, user.email)
                && Objects.equals(letterboxdUrl, user.letterboxdUrl)
                && Objects.equals(steamUrl, user.steamUrl)
                && Objects.equals(locale, user.locale)
                && Objects.equals(createdAt, user.createdAt)
                && Objects.equals(updatedAt, user.updatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name ,bio, avatarUrl, email, letterboxdUrl,steamUrl, locale, createdAt, updatedAt);
    }
}
