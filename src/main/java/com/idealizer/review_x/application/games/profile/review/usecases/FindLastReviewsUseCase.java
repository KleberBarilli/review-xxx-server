package com.idealizer.review_x.application.games.profile.review.usecases;

import com.idealizer.review_x.application.games.profile.review.responses.LastReviewItemResponse;
import com.idealizer.review_x.domain.profile.game.interfaces.SimpleProfileGame;
import com.idealizer.review_x.domain.profile.game.interfaces.SimpleProfileReview;
import com.idealizer.review_x.domain.profile.game.repositories.ProfileGameRepository;
import com.idealizer.review_x.domain.profile.game.repositories.ProfileReviewRepository;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class FindLastReviewsUseCase {

    private final ProfileReviewRepository profileReviewRepository;
    private final ProfileGameRepository profileGameRepository;

    public  FindLastReviewsUseCase(ProfileReviewRepository profileReviewRepository, ProfileGameRepository profileGameRepository) {
        this.profileReviewRepository = profileReviewRepository;
        this.profileGameRepository = profileGameRepository;
    }

    public List<LastReviewItemResponse> execute(ObjectId userId) {

        List<SimpleProfileReview> reviews = profileReviewRepository
                .findTop5ByUserIdOrderByCreatedAtDesc(userId);
        if (reviews.isEmpty()) return List.of();

        List<String> pgIds = reviews.stream()
                .map(SimpleProfileReview::getProfileGameId)
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
            SimpleProfileGame pg = pgMap.get(r.getProfileGameId());
            return new LastReviewItemResponse(
                    pg != null ? pg.getGameId() : null,
                    r.getProfileGameId() != null ? r.getProfileGameId() : null,
                    pg != null ? pg.getGameName() : null,
                    pg != null ? pg.getGameSlug() : null,
                    pg != null ? pg.getGameCover() : null,
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
