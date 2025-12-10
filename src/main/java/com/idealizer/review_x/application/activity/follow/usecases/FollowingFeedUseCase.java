package com.idealizer.review_x.application.activity.follow.usecases;

import com.idealizer.review_x.common.dtos.PageResponse;
import com.idealizer.review_x.common.dtos.activity.feed.FriendReviewFeedItem;
import com.idealizer.review_x.domain.core.activity.follow.repositories.FollowRepository;
import com.idealizer.review_x.domain.core.profile.game.interfaces.SimpleProfileGame;
import com.idealizer.review_x.domain.core.profile.game.repositories.ProfileGameRepository;
import com.idealizer.review_x.domain.core.review.interfaces.SimpleReview;
import com.idealizer.review_x.domain.core.review.repositories.ReviewRepository;
import org.bson.types.ObjectId;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class FollowingFeedUseCase {

    private final FollowRepository followRepository;
    private final ReviewRepository reviewRepository;
    private final ProfileGameRepository profileGameRepository;

    public FollowingFeedUseCase(FollowRepository followRepository, ReviewRepository reviewRepository,
                                ProfileGameRepository profileGameRepository
                                ) {
        this.followRepository = followRepository;
        this.reviewRepository = reviewRepository;
        this.profileGameRepository = profileGameRepository;
    }


    public PageResponse<FriendReviewFeedItem> execute(ObjectId userId, int pageNumber, int limit) {
        int page = Math.max(pageNumber, 0);
        int size = Math.max(limit, 1);

        var followeeIds = followRepository.findFolloweeIds(userId).stream()
                .map(FollowRepository.FolloweeIdOnly::getFolloweeId)
                .toList();

        if (followeeIds.isEmpty()) {
            return new PageResponse<>(page, size, 0, 0, true, List.of());
        }

        var pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        var reviews = reviewRepository.findProjectedByUserIdIn(followeeIds, pageable);

        if (reviews.isEmpty()) {
            return new PageResponse<>(reviews.getNumber(), reviews.getSize(),
                    reviews.getTotalElements(), reviews.getTotalPages(), reviews.isLast(), List.of());
        }
        var pgIds = reviews.getContent().stream()
                .map(SimpleReview::getProfileTargetId)
                .filter(Objects::nonNull)
                .distinct()
                .toList();

        var pgById = profileGameRepository.findProjectedByIdIn(pgIds).stream()
                .collect(Collectors.toMap(SimpleProfileGame::getId, x -> x));

        var items = reviews.getContent().stream()
                .map(r -> {
                    var key = r.getProfileTargetId() != null ? r.getProfileTargetId().toHexString() : null;
                    var pg = key != null ? pgById.get(key) : null;
                    return new FriendReviewFeedItem(
                            r.getUserId() != null ? r.getUserId().toHexString() : null,
                            r.getUsername(),
                            r.getTargetName(),
                            r.getTargetSlug(),
                            r.getTargetCover(),
                            r.getId() != null ? r.getId().toHexString() : null,
                            r.getTitle(),
                            r.getContent(),
                            r.getSpoiler(),
                            r.getReplay(),
                            r.getLikeCount(),
                            r.getCreatedAt(),
                            r.getUpdatedAt(),
                            pg != null ? pg.getRating() : null,
                            pg != null ? pg.getLiked() : null,
                            pg != null ? pg.getMastered() : null,
                            pg != null ? pg.getStatus() : null,
                            pg != null ? pg.getPlayedOn() : null,
                            pg != null ? pg.getSourcePlatform() : null,
                            pg != null ? pg.getFinishedAt() : null
                    );
                })
                .toList();

        return new PageResponse<>(
                reviews.getNumber(),
                reviews.getSize(),
                reviews.getTotalElements(),
                reviews.getTotalPages(),
                reviews.isLast(),
                items
        );
    }
}