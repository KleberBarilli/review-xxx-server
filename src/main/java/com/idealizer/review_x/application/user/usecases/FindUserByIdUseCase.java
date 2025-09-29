package com.idealizer.review_x.application.user.usecases;

import com.idealizer.review_x.domain.core.user.entities.User;
import com.idealizer.review_x.domain.core.user.repositories.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FindUserByIdUseCase {
    private final UserRepository userRepository;
    public FindUserByIdUseCase(
            UserRepository userRepository) {
        this.userRepository = userRepository;

    }
    public Optional<User> execute(ObjectId id) {
        return userRepository.findById(id);
    }
}
