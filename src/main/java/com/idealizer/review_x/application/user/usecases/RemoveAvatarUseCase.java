package com.idealizer.review_x.application.user.usecases;

import com.idealizer.review_x.domain.user.entities.User;
import com.idealizer.review_x.domain.user.repositories.UserRepository;
import com.idealizer.review_x.domain.user.services.AvatarStorageService;
import org.springframework.stereotype.Service;

@Service
public class RemoveAvatarUseCase {
    private final UserRepository userRepository;
    private final AvatarStorageService avatarStorageService;

    public RemoveAvatarUseCase(UserRepository userRepository, AvatarStorageService avatarStorageService) {
        this.userRepository = userRepository;
        this.avatarStorageService = avatarStorageService;
    }
    public User execute(String username) {
        User user = userRepository.findByName(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + username));

        if (user.getAvatarUrl() != null) {
            avatarStorageService.deleteAvatar(username,
                    user.getAvatarUrl().substring(user.getAvatarUrl().lastIndexOf('/') + 1));
        }

        user.setAvatarUrl(null);
        userRepository.save(user);

        return user;

    }
}
