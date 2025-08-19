package com.idealizer.review_x.infra.http.controllers.game.profile;

import com.idealizer.review_x.application.review.usecases.FindReviewByUsernameUseCase;
import com.idealizer.review_x.common.dtos.PageResponse;
import com.idealizer.review_x.common.dtos.review.FindReviewDTO;
import com.idealizer.review_x.domain.profile.game.entities.ProfileGameStatus;
import com.idealizer.review_x.domain.review.interfaces.BaseReview;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("api/reviews")
@Tag(name = "Reviews")
public class ReviewController {

    private final FindReviewByUsernameUseCase findReviewByUsernameUseCase;

    public ReviewController(FindReviewByUsernameUseCase findReviewByUsernameUseCase) {
        this.findReviewByUsernameUseCase = findReviewByUsernameUseCase;
    }

    @GetMapping("public/{username}")
    public PageResponse<BaseReview> getUserReviews(
            @PathVariable String username,
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "20") int limit,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) String order,
            @RequestParam(required = false)
            Set<ProfileGameStatus> status,
            @RequestParam(required = false) Integer ratingMin,
            @RequestParam(required = false) Integer ratingMax
    ) {

        FindReviewDTO f = new FindReviewDTO(
                pageNumber, limit, sort, order,
                status, ratingMin, ratingMax
        );
        return findReviewByUsernameUseCase.execute(username, f);

    }
}
