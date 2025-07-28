package com.idealizer.review_x.infra.http.modules.user;

import com.idealizer.review_x.application.user.responses.LoginResponse;
import com.idealizer.review_x.application.user.usecases.SignInUseCase;
import com.idealizer.review_x.application.user.usecases.SignUpUseCase;
import com.idealizer.review_x.common.LocaleUtil;
import com.idealizer.review_x.common.MessageUtil;
import com.idealizer.review_x.infra.http.modules.user.dto.LoginRequestDTO;
import com.idealizer.review_x.infra.http.modules.user.dto.SignupRequestDTO;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.core.AuthenticationException;

import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/api/users")
@Tag(name = "Users")
public class UserController {

    private final SignUpUseCase signUpUseCase;
    private final SignInUseCase signInUseCase;
    private final MessageUtil messageUtil;


    public UserController(SignUpUseCase signUpUseCase, SignInUseCase signInUseCase, MessageUtil messageUtil) {
        this.signUpUseCase = signUpUseCase;
        this.signInUseCase = signInUseCase;
        this.messageUtil = messageUtil;
    }

    @PostMapping("/signIn")
    public ResponseEntity<?> authenticateUser(@RequestBody @Valid LoginRequestDTO dto) {
        try {
            LoginResponse response = signInUseCase.execute(dto.username(), dto.password(), dto.locale());
            return ResponseEntity.ok(response);
        } catch (AuthenticationException exception) {
            Map<String, Object> map = new HashMap<>();
            map.put("message", messageUtil.get("user.badCredentials", null, LocaleUtil.from(dto.locale())));
            return new ResponseEntity<Object>(map, HttpStatus.UNAUTHORIZED);
        }

    }

    @PostMapping("/signUp")
    public ResponseEntity<?> registerUser(@RequestBody SignupRequestDTO signupRequestDTO) {
        try {
            signUpUseCase.execute(signupRequestDTO);
        } catch (Exception e) {
            Map<String, Object> map = new HashMap<>();
            map.put("message", e.getMessage());
            return new ResponseEntity<Object>(map, HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity.ok(Map.of(
                "message", messageUtil.get("user.registered", null, LocaleUtil.from(signupRequestDTO.locale()))
        ));
    }

}
