package com.idealizer.review_x.infra.http.modules.user;

import com.idealizer.review_x.application.user.responses.CurrentLoggedUserResponse;
import com.idealizer.review_x.application.user.responses.LoginResponse;
import com.idealizer.review_x.application.user.usecases.*;
import com.idealizer.review_x.common.LocaleUtil;
import com.idealizer.review_x.common.MessageUtil;
import com.idealizer.review_x.common.exceptions.DuplicatedException;
import com.idealizer.review_x.infra.http.modules.user.dto.LoginRequestDTO;
import com.idealizer.review_x.infra.http.modules.user.dto.SignupRequestDTO;
import com.idealizer.review_x.security.services.UserDetailsImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.bson.types.ObjectId;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.core.AuthenticationException;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/users")
@Tag(name = "Users")
public class UserController {
    private final Logger logger = Logger.getLogger(UserController.class.getName());

    private final SignUpUseCase signUpUseCase;
    private final SignInUseCase signInUseCase;
    private final FindCurrentLoggedUserUseCase findCurrentLoggedUserUseCase;
    private final UploadAvatarUseCase uploadAvatarUseCase;
    private final RemoveAvatarUseCase removeAvatarUseCase;
    private final MessageUtil messageUtil;

    public UserController(SignUpUseCase signUpUseCase, SignInUseCase signInUseCase,
            FindCurrentLoggedUserUseCase findCurrentLoggedUserUseCase,
            UploadAvatarUseCase uploadAvatarUseCase,
            RemoveAvatarUseCase removeAvatarUseCase, MessageUtil messageUtil) {
        this.signUpUseCase = signUpUseCase;
        this.signInUseCase = signInUseCase;
        this.findCurrentLoggedUserUseCase = findCurrentLoggedUserUseCase;
        this.uploadAvatarUseCase = uploadAvatarUseCase;
        this.removeAvatarUseCase = removeAvatarUseCase;
        this.messageUtil = messageUtil;
    }

    @PostMapping("/signIn")
    public ResponseEntity<?> authenticateUser(@RequestBody @Valid LoginRequestDTO dto) {
        Locale locale = LocaleContextHolder.getLocale();
        try {
            LoginResponse response = signInUseCase.execute(dto.username(), dto.password(), locale.toString());
            return ResponseEntity.ok(response);
        } catch (AuthenticationException exception) {
            Map<String, Object> map = new HashMap<>();
            map.put("message", messageUtil.get("user.badCredentials", null, LocaleUtil.from(locale.toString())));
            return new ResponseEntity<Object>(map, HttpStatus.UNAUTHORIZED);
        }

    }

    @PostMapping("/signUp")
    public ResponseEntity<?> registerUser(@RequestBody @Valid SignupRequestDTO signupRequestDTO) {
        Locale locale = LocaleContextHolder.getLocale();
        try {
            signUpUseCase.execute(signupRequestDTO, locale.toString());
        }
        catch (DuplicatedException e){
            Map<String, Object> map = new HashMap<>();
            map.put("message", e.getMessage());
            return new ResponseEntity<Object>(map, HttpStatus.BAD_REQUEST);
        }
        catch (Exception e) {
            Map<String, Object> map = new HashMap<>();
            logger.severe("Error during user registration: " + e.getMessage());
            map.put("message", messageUtil.get("signUp.error", null, LocaleUtil.from(locale.toString())));
            return new ResponseEntity<Object>(map, HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity.ok(Map.of(
                "message", messageUtil.get("user.registered", null, LocaleUtil.from(locale.toString()))));
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
        ObjectId userId = ((UserDetailsImpl) userDetails).getId();
        CurrentLoggedUserResponse user = findCurrentLoggedUserUseCase.execute(userId);

        return ResponseEntity.ok(user);
    }

    @PatchMapping("/updateAvatar")
    public ResponseEntity<?> updateAvatar(@AuthenticationPrincipal UserDetails userDetails,
            @RequestParam("file") MultipartFile file) {
        Locale locale = LocaleContextHolder.getLocale();
        try {

            String contentType = file.getContentType();
            if (contentType == null
                    || !List.of("image/jpeg", "image/png", "image/jpg", "image/webp").contains(contentType)) {
                return ResponseEntity.badRequest().body((messageUtil.get("file.image.invalid",
                        null, LocaleUtil.from(locale.toString()))));
            }

            if (file.getSize() > 5 * 1024 * 1024) {
                return ResponseEntity.badRequest().body(messageUtil.get("file.image.tooLarge",
                        null, LocaleUtil.from(locale.toString())));
            }
            String filename = file.getOriginalFilename();
            byte[] imageBytes = file.getBytes();
            uploadAvatarUseCase.execute(userDetails.getUsername(), imageBytes, filename, contentType);

            return ResponseEntity.ok(Map.of(
                    "message", messageUtil.get("file.image.uploaded",
                            null, LocaleUtil.from(locale.toString()))));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(messageUtil.get("file.image.upload.error", null,
                    LocaleUtil.from(locale.toString())));
        }
    }

    @DeleteMapping("/removeAvatar")
    public ResponseEntity<?> removeAvatar(@AuthenticationPrincipal UserDetails userDetails) {
        try {
            removeAvatarUseCase.execute(userDetails.getUsername());
            return ResponseEntity.ok(Map.of(
                    "message", "Avatar removed successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(
                    messageUtil.get("file.image.remove.error", null,
                            LocaleUtil.from(LocaleContextHolder.getLocale().toString())));
        }
    }
}
