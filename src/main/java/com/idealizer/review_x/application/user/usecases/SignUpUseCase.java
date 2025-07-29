package com.idealizer.review_x.application.user.usecases;

import com.idealizer.review_x.application.user.mappers.UserMapper;
import com.idealizer.review_x.application.user.validators.UserValidator;
import com.idealizer.review_x.common.LocaleUtil;
import com.idealizer.review_x.common.MessageUtil;
import com.idealizer.review_x.common.exceptions.DuplicatedException;
import com.idealizer.review_x.domain.user.entities.User;
import com.idealizer.review_x.domain.user.repositories.UserRepository;
import com.idealizer.review_x.infra.http.modules.user.dto.SignupRequestDTO;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.logging.Logger;

@Service
public class SignUpUseCase {
    private static final Logger logger = Logger.getLogger(SignUpUseCase.class.getName());

    private final UserRepository userRepository;
    private final UserValidator userValidator;
    private final UserMapper userMapper;
    private final MessageUtil messageUtil;
    private final PasswordEncoder passwordEncoder;

    public SignUpUseCase(
            UserRepository userRepository,
            UserValidator userValidator,
            UserMapper userMapper,
            MessageUtil messageUtil,
            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userValidator = userValidator;
        this.userMapper = userMapper;
        this.messageUtil = messageUtil;
        this.passwordEncoder = passwordEncoder;
    }

    public void execute(SignupRequestDTO dto, String locale) {
        Optional<User> userOptional = userRepository.findByEmailOrName(dto.email(), dto.name());

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            if (user.getEmail().equals(dto.email())) {
                throw new DuplicatedException(messageUtil.get("user.email.exists", null, LocaleUtil.from(locale)));
            }

            if (user.getName().equals(dto.name())) {
                throw new DuplicatedException(messageUtil.get("user.name.exists", null, LocaleUtil.from(locale)));
            }
        }

        User user = userMapper.toEntity(dto);

        userValidator.validate(user);
        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);
        userRepository.save(user);

        logger.info("New user created: " + user.getName() + " with email: " + user.getEmail());

    }
}
