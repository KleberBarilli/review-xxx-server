package com.idealizer.review_x.application.user.usecases;

import com.idealizer.review_x.domain.user.entities.User;
import com.idealizer.review_x.domain.user.repositories.UserRepository;
import com.idealizer.review_x.domain.user.services.AvatarStorageService;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
public class UploadAvatarUseCase {
    private static final Logger logger = Logger.getLogger(UploadAvatarUseCase.class.getName());

    private final UserRepository userRepository;
    private final AvatarStorageService avatarStorageService;
    private final RemoveAvatarUseCase removeAvatarUseCase;

    public UploadAvatarUseCase( UserRepository userRepository, AvatarStorageService avatarStorageService,
                                RemoveAvatarUseCase removeAvatarUseCase) {
        this.userRepository = userRepository;
        this.avatarStorageService = avatarStorageService;
        this.removeAvatarUseCase = removeAvatarUseCase;
    }
    public void execute(String username, byte[] imageData, String filename, String contentType) {

        User user = removeAvatarUseCase.execute(username);
        String url = avatarStorageService.uploadAvatar(username, imageData, filename, contentType);
        user.setAvatarUrl(url);
        userRepository.save(user);
        logger.info("Avatar uploaded successfully for user:" + username);
    }

}
