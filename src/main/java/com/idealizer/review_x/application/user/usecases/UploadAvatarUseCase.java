package com.idealizer.review_x.application.user.usecases;

import com.idealizer.review_x.domain.core.user.entities.User;
import com.idealizer.review_x.domain.core.user.repositories.UserRepository;
import com.idealizer.review_x.domain.core.user.services.AvatarStorageService;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
public class UploadAvatarUseCase {

    private final UserRepository userRepository;
    private final AvatarStorageService avatarStorageService;
    private final RemoveAvatarUseCase removeAvatarUseCase;

    public UploadAvatarUseCase( UserRepository userRepository, AvatarStorageService avatarStorageService,
                                RemoveAvatarUseCase removeAvatarUseCase) {
        this.userRepository = userRepository;
        this.avatarStorageService = avatarStorageService;
        this.removeAvatarUseCase = removeAvatarUseCase;
    }
    public String execute(String username, byte[] imageData, String filename, String contentType) {
        User user = removeAvatarUseCase.execute(username);
        String url = avatarStorageService.uploadAvatar(username, imageData, filename, contentType);
        user.setAvatarUrl(url);
        userRepository.save(user);
        return url;
    }

}
