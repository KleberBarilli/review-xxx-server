package com.idealizer.review_x.application.modules.users.services;

import com.idealizer.review_x.application.modules.users.entities.User;
import com.idealizer.review_x.application.modules.users.repositories.UserRepository;
import com.idealizer.review_x.application.modules.users.validators.UserValidator;
import org.springframework.stereotype.Service;

@Service
public class CreateUserService {
    private final UserRepository userRepository;
    private final UserValidator userValidator;

    public CreateUserService(UserRepository userRepository, UserValidator userValidator) {
        this.userRepository = userRepository;
        this.userValidator = userValidator;
    }

    public String execute(User user){
        this.userValidator.validate(user);
        this.userRepository.save(user);
        return user.getId().toHexString();

    }
}
