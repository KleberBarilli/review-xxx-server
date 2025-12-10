package com.idealizer.review_x.infra.http.controllers.activity.feed;

import com.idealizer.review_x.application.activity.follow.usecases.FollowingFeedUseCase;
import com.idealizer.review_x.common.dtos.PageResponse;
import com.idealizer.review_x.common.dtos.activity.feed.FriendReviewFeedItem;
import com.idealizer.review_x.security.services.UserDetailsImpl;
import org.bson.types.ObjectId;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/feed")
public class FeedController {

    private final FollowingFeedUseCase feedService;

    public FeedController(FollowingFeedUseCase feedService) {
        this.feedService = feedService;
    }

    @GetMapping("/following")
    public PageResponse<FriendReviewFeedItem> following(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {

        ObjectId userId = ((UserDetailsImpl) userDetails).getId();

        return feedService.execute(userId, page, size);
    }


}
