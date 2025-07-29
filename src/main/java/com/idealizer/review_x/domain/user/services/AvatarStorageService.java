package com.idealizer.review_x.domain.user.services;

public interface AvatarStorageService {
    String uploadAvatar(String userId, byte[] imageData, String filename, String contentType);
    void deleteAvatar(String userId, String filename);
}
