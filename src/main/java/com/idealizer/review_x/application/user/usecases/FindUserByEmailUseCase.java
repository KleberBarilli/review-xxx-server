package com.idealizer.review_x.application.user.usecases;

import com.idealizer.review_x.domain.core.user.entities.User;
import com.idealizer.review_x.domain.core.user.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FindUserByEmailUseCase {
    private final UserRepository userRepository;

    public FindUserByEmailUseCase(
            UserRepository userRepository) {
        this.userRepository = userRepository;

    }

    public Optional<User> execute(String email) {
        return userRepository.findByEmail(email);
    }
}
