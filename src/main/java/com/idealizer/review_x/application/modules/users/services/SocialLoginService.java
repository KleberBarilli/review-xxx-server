package com.idealizer.review_x.application.modules.users.services;

import com.idealizer.review_x.application.modules.users.entities.User;
import com.idealizer.review_x.application.modules.users.repositories.UserRepository;
import com.idealizer.review_x.application.modules.users.services.mappers.UserMapper;
import com.idealizer.review_x.application.modules.users.services.output.SocialLoginOutput;
import com.idealizer.review_x.application.modules.users.validators.UserValidator;
import com.idealizer.review_x.common.LocaleUtil;
import com.idealizer.review_x.common.MessageUtil;
import com.idealizer.review_x.infra.http.modules.user.dto.SocialLoginDTO;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.logging.Logger;

@Service
public class SocialLoginService {

    private final UserRepository userRepository;
    private final UserValidator userValidator;
    private final UserMapper userMapper;
    private final MessageUtil messageUtil;
    private static final Logger logger = Logger.getLogger(SocialLoginService.class.getName());

    public SocialLoginService(
            UserRepository userRepository,
            UserValidator userValidator,
            UserMapper userMapper,
            MessageUtil messageUtil

    ) {
        this.userRepository = userRepository;
        this.userValidator = userValidator;
        this.userMapper = userMapper;
        this.messageUtil = messageUtil;
    }

    public SocialLoginOutput execute(SocialLoginDTO dto) {
        Optional<User> userOptional = userRepository.findByEmail(dto.email());

        if (userOptional.isEmpty()) {
            logger.info(
                    messageUtil.get("user.email.notFound", null, LocaleUtil.from(dto.locale()))
            );

            User user = userMapper.toEntity(dto);
            userValidator.validate(user);
            userRepository.save(user);

            return new SocialLoginOutput("jwt token for new user");
        }

        return new SocialLoginOutput("jwt token for existing user");
    }
}
