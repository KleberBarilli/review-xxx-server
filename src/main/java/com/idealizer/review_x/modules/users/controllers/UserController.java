package com.idealizer.review_x.modules.users.controllers;

import com.idealizer.review_x.common.LocaleUtil;
import com.idealizer.review_x.common.MessageUtil;
import com.idealizer.review_x.modules.users.controllers.dto.SocialLoginDTO;
import com.idealizer.review_x.modules.users.controllers.dto.SocialLoginResponseDTO;
import com.idealizer.review_x.modules.users.controllers.mappers.UserMapper;
import com.idealizer.review_x.modules.users.entities.User;
import com.idealizer.review_x.modules.users.services.CreateUserService;
import com.idealizer.review_x.modules.users.services.FindUserByEmailService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@RestController
@RequestMapping("/api/users")
@Tag(name = "Users")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private MessageUtil messageUtil;

    private final CreateUserService createUserService;
    private FindUserByEmailService findUserByEmailService;

    private final UserMapper userMapper;

    public UserController(CreateUserService createUserService, FindUserByEmailService findUserByEmailService, UserMapper userMapper,
                          MessageUtil messageUtil)  {
        this.createUserService = createUserService;
        this.findUserByEmailService = findUserByEmailService;
        this.userMapper = userMapper;
        this.messageUtil = messageUtil;
    }

    @PostMapping("/auth/")
    public ResponseEntity<SocialLoginResponseDTO> auth(@RequestBody @Valid SocialLoginDTO dto) {

        Optional<User> userOptional = findUserByEmailService.execute(dto.email());

        if (userOptional.isEmpty()) {
            logger.info(messageUtil.get("user.email.notFound", null, LocaleUtil.from(dto.locale())), dto.email());
            User user = userMapper.toEntity(dto);
            this.createUserService.execute(user);
            return ResponseEntity.ok(new SocialLoginResponseDTO("jwt token"));
        }
        return ResponseEntity.ok(new SocialLoginResponseDTO("jwt token for existing user"));
    }

}
