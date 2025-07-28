package com.idealizer.review_x.application.user.usecases;

import com.idealizer.review_x.application.user.mappers.UserMapper;
import com.idealizer.review_x.application.user.responses.CurrentLoggedUserResponse;
import com.idealizer.review_x.domain.user.entities.User;
import com.idealizer.review_x.domain.user.repositories.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class FindCurrentLoggedUserUseCase {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public FindCurrentLoggedUserUseCase(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public CurrentLoggedUserResponse execute(String name) {
        return userRepository.findByName(name)
                .map(userMapper::toCurrentLoggedUser)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
