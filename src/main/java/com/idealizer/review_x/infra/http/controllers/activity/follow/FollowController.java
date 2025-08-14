package com.idealizer.review_x.infra.http.controllers.activity.follow;

import com.idealizer.review_x.application.activity.follow.usecases.FollowUseCase;
import com.idealizer.review_x.application.activity.follow.usecases.UnfollowUseCase;
import com.idealizer.review_x.security.services.UserDetailsImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/activities/follow")
@Tag(name = "Follow")
public class FollowController {

    private final FollowUseCase followUseCase;
    private final UnfollowUseCase unfollowUseCase;

    public FollowController(FollowUseCase followUseCase, UnfollowUseCase unfollowUseCase) {
        this.followUseCase = followUseCase;
        this.unfollowUseCase = unfollowUseCase;
    }

    @PutMapping("/{followedId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void follow(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable String followedId
    ) {
        ObjectId userId = ((UserDetailsImpl) userDetails).getId();
        followUseCase.execute(userId, new ObjectId(followedId));
    }

    @DeleteMapping("/{followedId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void unfollow(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable String followedId
    ) {
        ObjectId userId = ((UserDetailsImpl) userDetails).getId();
        unfollowUseCase.execute(userId, new ObjectId(followedId));
    }
}
