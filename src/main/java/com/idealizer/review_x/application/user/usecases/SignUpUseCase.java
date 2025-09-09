package com.idealizer.review_x.application.user.usecases;

import com.idealizer.review_x.application.user.mappers.UserMapper;
import com.idealizer.review_x.application.user.validators.UserValidator;
import com.idealizer.review_x.common.CommonError;
import com.idealizer.review_x.common.exceptions.DuplicatedException;
import com.idealizer.review_x.domain.core.user.entities.User;
import com.idealizer.review_x.domain.core.user.repositories.UserRepository;
import com.idealizer.review_x.infra.http.controllers.user.dto.SignupRequestDTO;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
public class SignUpUseCase {
    private static final Logger logger = Logger.getLogger(SignUpUseCase.class.getName());

    private final UserRepository userRepository;
    private final UserValidator userValidator;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public SignUpUseCase(
            UserRepository userRepository,
            UserValidator userValidator,
            UserMapper userMapper,
            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userValidator = userValidator;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public void execute(SignupRequestDTO dto) {
            if (userRepository.existsByEmail(dto.email())) {
                throw new DuplicatedException(CommonError.EMAIL_OR_USERNAME_ALREADY_EXISTS.code());
            }
            if (userRepository.existsByName(dto.name())) {
                throw new DuplicatedException(CommonError.EMAIL_OR_USERNAME_ALREADY_EXISTS.code());
            }
        User user = userMapper.toEntity(dto);

        userValidator.validate(user);
        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);
        userRepository.save(user);

        logger.info("New user created: " + user.getName() + " with email: " + user.getEmail());

    }
}
