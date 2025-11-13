package com.idealizer.review_x.application.user.usecases;

import com.idealizer.review_x.application.user.mappers.UserMapper;
import com.idealizer.review_x.common.dtos.user.UpdateUserDTO;
import com.idealizer.review_x.domain.core.user.entities.User;
import com.idealizer.review_x.domain.core.user.repositories.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

@Service
public class UpdateUserUseCase {

    private UserRepository userRepository;
    private UserMapper userMapper;

    public UpdateUserUseCase(UserRepository userRepository,  UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public void execute(ObjectId userId, UpdateUserDTO dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        userMapper.updateUserFromDto(dto, user);
        userRepository.save(user);
    }
}
