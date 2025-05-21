package com.idealizer.review_x.modules.users.controllers;

import com.idealizer.review_x.modules.users.controllers.mappers.UserMapper;
import com.idealizer.review_x.modules.users.entities.User;
import com.idealizer.review_x.modules.users.services.FindUserByEmailService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@RestController
@RequestMapping("/api/users")
@Tag(name="Users")
public class UserController {
    private static final Logger logger  = LoggerFactory.getLogger(UserController.class);

    private FindUserByEmailService findUserByEmailService;

    private final UserMapper userMapper;
    public UserController(FindUserByEmailService findUserByEmailService, UserMapper userMapper) {
        this.findUserByEmailService = findUserByEmailService;
        this.userMapper = userMapper;
    }

    @PostMapping("/auth/{email}")
    public User auth(@RequestParam(value = "email") String email) {

        Optional<User> userOptional = findUserByEmailService.execute(email);

        if (userOptional.isEmpty()) {
            logger.info("User not found with email: {}", email);
            return null;
        }
        return ResponseEntity.ok(userOptional.get()).getBody();
    }

}
