package com.idealizer.review_x.modules.users.services;


import com.idealizer.review_x.modules.users.entities.User;
import com.idealizer.review_x.modules.users.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FindUserByEmailService {
    private final UserRepository userRepository;
    public FindUserByEmailService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    public Optional<User> execute(String email){
        return userRepository.findByEmail(email);
    }
}
