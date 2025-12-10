package com.idealizer.review_x.infra.http.controllers.activity.follow;

import com.idealizer.review_x.application.activity.follow.usecases.FindFollowersUseCase;
import com.idealizer.review_x.application.activity.follow.usecases.FindFollowingUseCase;
import com.idealizer.review_x.application.activity.follow.usecases.FollowUseCase;
import com.idealizer.review_x.application.activity.follow.usecases.UnfollowUseCase;
import com.idealizer.review_x.common.dtos.activity.follow.FollowerResponse;
import com.idealizer.review_x.domain.core.activity.follow.entities.Follow;
import com.idealizer.review_x.security.services.UserDetailsImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/activities/follow")
@Tag(name = "Follow")
public class FollowController {

    private final FollowUseCase followUseCase;
    private final UnfollowUseCase unfollowUseCase;
    private final FindFollowersUseCase findFollowersUseCase;
    private final FindFollowingUseCase findFollowingUseCase;

    public FollowController(FollowUseCase followUseCase, UnfollowUseCase unfollowUseCase,
                            FindFollowersUseCase findFollowersUseCase,
                            FindFollowingUseCase findFollowingUseCase
                            ) {
        this.followUseCase = followUseCase;
        this.unfollowUseCase = unfollowUseCase;
        this.findFollowersUseCase = findFollowersUseCase;
        this.findFollowingUseCase = findFollowingUseCase;
    }

    @GetMapping("/followers/{userId}")
    public List<FollowerResponse> followers(@AuthenticationPrincipal UserDetails userDetails, @PathVariable String userId){

        ObjectId loggedUserId = ((UserDetailsImpl) userDetails).getId();
        return findFollowersUseCase.execute(new ObjectId(userId), loggedUserId);
    }

    @GetMapping("/following/{userId}")
    public List<FollowerResponse> following(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable String userId
    ) {
        ObjectId loggedUserId = ((UserDetailsImpl) userDetails).getId();
        return findFollowingUseCase.execute(new ObjectId(userId), loggedUserId);
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
