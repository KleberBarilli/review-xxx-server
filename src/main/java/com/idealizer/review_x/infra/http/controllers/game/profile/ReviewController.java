package com.idealizer.review_x.infra.http.controllers.game.profile;

import com.idealizer.review_x.application.review.usecases.FindReviewByUsernameUseCase;
import com.idealizer.review_x.application.review.usecases.FindTimelineByUsernameUseCase;
import com.idealizer.review_x.common.dtos.FindTimelineDTO;
import com.idealizer.review_x.common.dtos.PageResponse;
import com.idealizer.review_x.common.dtos.review.FindReviewDTO;
import com.idealizer.review_x.domain.core.profile.BaseTimelineItem;
import com.idealizer.review_x.domain.core.profile.game.entities.ProfileGameStatus;
import com.idealizer.review_x.domain.core.review.interfaces.BaseReview;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("api/reviews")
@Tag(name = "Reviews")
public class ReviewController {

    private final FindReviewByUsernameUseCase findReviewByUsernameUseCase;
    private final FindTimelineByUsernameUseCase findTimelineByUsernameUseCase;

    public ReviewController(FindReviewByUsernameUseCase findReviewByUsernameUseCase,
                            FindTimelineByUsernameUseCase timelineByUsernameUseCase
    ) {
        this.findReviewByUsernameUseCase = findReviewByUsernameUseCase;
        this.findTimelineByUsernameUseCase = timelineByUsernameUseCase;
    }

    @GetMapping("public/{username}")
    public PageResponse<BaseReview> getUserReviews(
            @PathVariable String username,
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "20") int limit,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) String order,
            @RequestParam(required = false) Set<ProfileGameStatus> status,
            @RequestParam(required = false) Integer ratingMin,
            @RequestParam(required = false) Integer ratingMax
    ) {

        FindReviewDTO f = new FindReviewDTO(
                pageNumber, limit, sort, order,
                status, ratingMin, ratingMax
        );
        return findReviewByUsernameUseCase.execute(username, f);

    }

    @GetMapping("/public/{username}/timeline")
    public PageResponse<BaseTimelineItem> getTimeline(
            @PathVariable String username,
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "20") int limit,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false, name = "types") List<String> types,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) String order
    ) {
        FindTimelineDTO dto = new FindTimelineDTO(pageNumber, limit, sort, order, year, types );
        return findTimelineByUsernameUseCase.execute(username, dto);
    }
}
