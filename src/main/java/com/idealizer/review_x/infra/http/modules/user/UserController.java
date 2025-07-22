package com.idealizer.review_x.infra.http.modules.user;

import com.idealizer.review_x.application.modules.users.services.SocialLoginService;
import com.idealizer.review_x.infra.http.modules.user.dto.SocialLoginDTO;
import com.idealizer.review_x.application.modules.users.services.output.SocialLoginOutput;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/users")
@Tag(name = "Users")
public class UserController {

    private final SocialLoginService socialLoginService;

    public UserController(SocialLoginService socialLoginService
    ) {
        this.socialLoginService = socialLoginService;
    }
    @PostMapping("/auth/")
    public ResponseEntity<SocialLoginOutput> auth(@RequestBody @Valid SocialLoginDTO dto) {

        SocialLoginOutput output = socialLoginService.execute(dto);
        return ResponseEntity.ok(output);
    }

}
