package com.idealizer.review_x.infra.http.controllers.activity.like;

import com.idealizer.review_x.application.activity.like.usecases.CreateLikeUseCase;
import com.idealizer.review_x.application.activity.like.usecases.UnlikeUseCase;
import com.idealizer.review_x.common.dtos.activity.like.LikeDTO;
import com.idealizer.review_x.security.services.UserDetailsImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/activities/likes")
@Tag(name = "Likes")
public class LikeController {

    private final CreateLikeUseCase createLikeUseCase;
    private final UnlikeUseCase unlikeUseCase;

    public LikeController(CreateLikeUseCase createLikeUseCase,
                          UnlikeUseCase unlikeUseCase)  {
        this.createLikeUseCase = createLikeUseCase;
        this.unlikeUseCase = unlikeUseCase;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void createLike(@AuthenticationPrincipal UserDetails userDetails, @RequestBody @Valid LikeDTO dto) {

        ObjectId userId = ((UserDetailsImpl) userDetails).getId();
        createLikeUseCase.execute(userId, new ObjectId(dto.targetId()), dto.targetType());
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void unlike(@AuthenticationPrincipal UserDetails userDetails, @RequestBody @Valid LikeDTO dto) {

        ObjectId userId = ((UserDetailsImpl) userDetails).getId();
        unlikeUseCase.execute(userId, new ObjectId(dto.targetId()), dto.targetType());
    }

}
