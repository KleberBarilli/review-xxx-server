package com.idealizer.review_x.application.user.usecases;

import com.idealizer.review_x.domain.core.user.repositories.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class UpdateLastLoginUseCase {

    private final UserRepository userRepository;

    public UpdateLastLoginUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Async
    public void execute(ObjectId userId) {
        try {
            userRepository.updateLastLogin(userId, Instant.now());
        } catch (Exception e) {
            System.err.println("Failed to update last login: " + e.getMessage());
        }
    }
}