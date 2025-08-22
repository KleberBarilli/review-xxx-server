package com.idealizer.review_x.infra.http.controllers.user;

import com.idealizer.review_x.application.user.responses.FindUserResponse;
import com.idealizer.review_x.application.user.responses.LoginResponse;
import com.idealizer.review_x.application.user.usecases.*;
import com.idealizer.review_x.common.CommonError;
import com.idealizer.review_x.common.LocaleUtil;
import com.idealizer.review_x.common.MessageUtil;
import com.idealizer.review_x.common.dtos.FindUserArgsDTO;
import com.idealizer.review_x.common.exceptions.DuplicatedException;
import com.idealizer.review_x.infra.http.controllers.user.dto.LoginRequestDTO;
import com.idealizer.review_x.infra.http.controllers.user.dto.SignupRequestDTO;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
    private final FindUserByNameUseCase findUserByNameUseCase;
    private final UploadAvatarUseCase uploadAvatarUseCase;
    private final RemoveAvatarUseCase removeAvatarUseCase;
    private final MessageUtil messageUtil;

    public UserController(SignUpUseCase signUpUseCase, SignInUseCase signInUseCase, FindUserByNameUseCase findUserByNameUseCase,
                          UploadAvatarUseCase uploadAvatarUseCase, RemoveAvatarUseCase removeAvatarUseCase, MessageUtil messageUtil) {
        this.signUpUseCase = signUpUseCase;
        this.signInUseCase = signInUseCase;
        this.findUserByNameUseCase = findUserByNameUseCase;
        this.uploadAvatarUseCase = uploadAvatarUseCase;
        this.removeAvatarUseCase = removeAvatarUseCase;
        this.messageUtil = messageUtil;
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody @Valid LoginRequestDTO dto) {
        try {
            LoginResponse response = signInUseCase.execute(dto.identifier(), dto.password());
            return ResponseEntity.ok(response);
        } catch (AuthenticationException exception) {
            Map<String, Object> map = new HashMap<>();
            map.put("code", CommonError.INVALID_CREDENTIALS.code());
            return new ResponseEntity<Object>(map, CommonError.INVALID_CREDENTIALS.status());
        }

    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody @Valid SignupRequestDTO signupRequestDTO) {
        Locale locale = LocaleContextHolder.getLocale();
        try {
            signUpUseCase.execute(signupRequestDTO);
        } catch (DuplicatedException e) {
            Map<String, Object> map = new HashMap<>();
            map.put("code", e.getMessage());
            return new ResponseEntity<Object>(map, HttpStatus.CONFLICT);
        } catch (Exception e) {
            Map<String, Object> map = new HashMap<>();
            logger.severe("Error during user registration: " + e.getMessage());
            map.put("message", CommonError.CREATE_USER_GENERIC_ERROR );
            return new ResponseEntity<Object>(map, HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity.ok(Map.of("message", messageUtil.get("user.registered", null, LocaleUtil.from(locale.toString()))));
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(
            @RequestParam(name = "favorite", defaultValue = "false") boolean favorite,
            @RequestParam(name = "lastReviews", defaultValue = "false") boolean lastReviews,
            @RequestParam(name = "lastActivities", defaultValue = "false") boolean lastActivities,
            @AuthenticationPrincipal UserDetails userDetails) {
        FindUserArgsDTO args = new FindUserArgsDTO(favorite, lastReviews, lastActivities);
        FindUserResponse user = findUserByNameUseCase.execute(userDetails.getUsername(), args);

        return ResponseEntity.ok(user);
    }

    @GetMapping("/public/u/{username}")
    public ResponseEntity<?> findByUsername(
            @PathVariable String username,
            @RequestParam(name = "favorite", defaultValue = "false") boolean favorite,
            @RequestParam(name = "lastReviews", defaultValue = "false") boolean lastReviews,
            @RequestParam(name = "lastActivities", defaultValue = "false") boolean lastActivities
    ) {
        FindUserArgsDTO args = new FindUserArgsDTO(favorite, lastReviews, lastActivities);
        FindUserResponse user = findUserByNameUseCase.execute(username, args);

        if(user == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(user);
    }

    @PatchMapping("/updateAvatar")
    public ResponseEntity<?> updateAvatar(@AuthenticationPrincipal UserDetails userDetails, @RequestParam("file") MultipartFile file) {
        Locale locale = LocaleContextHolder.getLocale();
        try {

            String contentType = file.getContentType();
            if (contentType == null || !List.of("image/jpeg", "image/png", "image/jpg", "image/webp").contains(contentType)) {
                return ResponseEntity.badRequest().body((messageUtil.get("file.image.invalid", null, LocaleUtil.from(locale.toString()))));
            }

            if (file.getSize() > 5 * 1024 * 1024) {
                return ResponseEntity.badRequest().body(messageUtil.get("file.image.tooLarge", null, LocaleUtil.from(locale.toString())));
            }
            String filename = file.getOriginalFilename();
            byte[] imageBytes = file.getBytes();
            uploadAvatarUseCase.execute(userDetails.getUsername(), imageBytes, filename, contentType);

            return ResponseEntity.ok(Map.of("message", messageUtil.get("file.image.uploaded", null, LocaleUtil.from(locale.toString()))));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(messageUtil.get("file.image.upload.error", null, LocaleUtil.from(locale.toString())));
        }
    }

    @DeleteMapping("/removeAvatar")
    public ResponseEntity<?> removeAvatar(@AuthenticationPrincipal UserDetails userDetails) {
        try {
            removeAvatarUseCase.execute(userDetails.getUsername());
            return ResponseEntity.ok(Map.of("message", "Avatar removed successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(messageUtil.get("file.image.remove.error", null, LocaleUtil.from(LocaleContextHolder.getLocale().toString())));
        }
    }
}
