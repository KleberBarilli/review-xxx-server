package com.idealizer.review_x.application.review.usecases;

import com.idealizer.review_x.application.review.responses.LastReviewItemResponse;
import com.idealizer.review_x.domain.core.LogID;
import com.idealizer.review_x.domain.core.profile.game.interfaces.SimpleProfileGame;
import com.idealizer.review_x.domain.core.profile.game.interfaces.SimpleProfileReview;
import com.idealizer.review_x.domain.core.profile.game.repositories.ProfileGameRepository;
import com.idealizer.review_x.domain.core.review.repositories.ReviewRepository;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class FindLastReviewsByUserUseCase {

    private final ReviewRepository reviewRepository;
    private final ProfileGameRepository profileGameRepository;

    public FindLastReviewsByUserUseCase(ReviewRepository reviewRepository, ProfileGameRepository profileGameRepository) {
        this.reviewRepository = reviewRepository;
        this.profileGameRepository = profileGameRepository;
    }

    public List<LastReviewItemResponse> execute(ObjectId userId) {

        List<SimpleProfileReview> reviews = reviewRepository
                .findTop5ByUserIdOrderByCreatedAtDesc(userId);
        if (reviews.isEmpty()) return List.of();

        List<String> pgIds = reviews.stream()
                .map(SimpleProfileReview::getProfileTargetId)
                .filter(Objects::nonNull)
                .distinct()
                .toList();

        List<ObjectId> pgObjectIds = pgIds.stream()
                .filter(ObjectId::isValid)
                .map(ObjectId::new)
                .toList();

        Map<String, SimpleProfileGame> pgMap = profileGameRepository.findByIdIn(pgObjectIds).stream()
                .collect(Collectors.toMap(SimpleProfileGame::getId, Function.identity()));

        return reviews.stream().map(r -> {
            SimpleProfileGame pg = pgMap.get(r.getProfileTargetId());
            return new LastReviewItemResponse(
                    pg != null ? pg.getGameId() : null,
                    r.getProfileTargetId() != null ? r.getProfileTargetId() : null,
                    pg != null ? pg.getGameName() : null,
                    pg != null ? pg.getGameSlug() : null,
                    pg != null ? pg.getGameCover() : null,
                    LogID.GAMES,
                    pg != null ? pg.getLiked() : null,
                    pg != null ? pg.getRating() : null,
                    pg != null ? pg.getStatus() : null,
                    r.getContent(),
                    r.getSpoiler(),
                    r.getLikeCount(),
                    r.getCreatedAt()
            );
        }).toList();
    }
}
