package com.idealizer.review_x.infra.http.modules.user;

import com.idealizer.review_x.application.user.responses.CurrentLoggedUserResponse;
import com.idealizer.review_x.application.user.responses.LoginResponse;
import com.idealizer.review_x.application.user.usecases.*;
import com.idealizer.review_x.common.LocaleUtil;
import com.idealizer.review_x.common.MessageUtil;
import com.idealizer.review_x.infra.http.modules.user.dto.LoginRequestDTO;
import com.idealizer.review_x.infra.http.modules.user.dto.SignupRequestDTO;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.core.AuthenticationException;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/api/users")
@Tag(name = "Users")
public class UserController {

    private final SignUpUseCase signUpUseCase;
    private final SignInUseCase signInUseCase;
    private final FindCurrentLoggedUserUseCase findCurrentLoggedUserUseCase;
    private final UploadAvatarUseCase uploadAvatarUseCase;
    private final RemoveAvatarUseCase removeAvatarUseCase;
    private final MessageUtil messageUtil;


    public UserController(SignUpUseCase signUpUseCase, SignInUseCase signInUseCase,
                          FindCurrentLoggedUserUseCase findCurrentLoggedUserUseCase,
                          UploadAvatarUseCase uploadAvatarUseCase,
            RemoveAvatarUseCase removeAvatarUseCase,MessageUtil messageUtil) {
        this.signUpUseCase = signUpUseCase;
        this.signInUseCase = signInUseCase;
        this.findCurrentLoggedUserUseCase = findCurrentLoggedUserUseCase;
        this.uploadAvatarUseCase = uploadAvatarUseCase;
        this.removeAvatarUseCase = removeAvatarUseCase;
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
    public ResponseEntity<?> registerUser(@RequestBody @Valid SignupRequestDTO signupRequestDTO) {
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

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {

        CurrentLoggedUserResponse user = findCurrentLoggedUserUseCase.execute(userDetails.getUsername());

        return ResponseEntity.ok(user);
    }

    @PatchMapping("/updateAvatar")
    public ResponseEntity<?> updateAvatar(@AuthenticationPrincipal UserDetails userDetails,
                                          @RequestParam("file") MultipartFile file) {
        try {
            String filename = file.getOriginalFilename();
            String contentType = file.getContentType();
            byte[] imageBytes = file.getBytes();
            uploadAvatarUseCase.execute(userDetails.getUsername(), imageBytes, filename, contentType);

            return ResponseEntity.ok(Map.of(
                    "message", "Avatar uploaded successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An error occurred while uploading the avatar. Please try again later.");
        }
    }

    @DeleteMapping("/removeAvatar")
    public ResponseEntity<?> removeAvatar(@AuthenticationPrincipal UserDetails userDetails) {
    try {
            removeAvatarUseCase.execute(userDetails.getUsername());
            return ResponseEntity.ok(Map.of(
                    "message", "Avatar removed successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error removing avatar: " + e.getMessage());
        }
    }

}
