package com.idealizer.review_x.application.games.profile.game.usecases;

import com.idealizer.review_x.application.games.profile.game.responses.PublicProfileGameResponse;
import com.idealizer.review_x.domain.profile.game.interfaces.SimpleProfileGame;
import com.idealizer.review_x.domain.profile.game.repositories.ProfileGameRepository;
import com.idealizer.review_x.domain.profile.game.repositories.ProfileReviewRepository;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.logging.Logger;


@Service
public class FindProfileGameBySlugAndUsernameUseCase {
    private final Logger logger = Logger.getLogger(FindProfileGameBySlugAndUsernameUseCase.class.getName());
//todocache
    private final ProfileGameRepository profileGameRepository;
    private final ProfileReviewRepository profileReviewRepository;

    public FindProfileGameBySlugAndUsernameUseCase(ProfileGameRepository profileGameRepository,
                                                   ProfileReviewRepository profileReviewRepository) {
        this.profileGameRepository = profileGameRepository;
        this.profileReviewRepository = profileReviewRepository;
    }

    public Optional<PublicProfileGameResponse> execute(String gameSlug, String username) {
        Optional<SimpleProfileGame> pgOpt = profileGameRepository.findProjectedByUsernameAndGameSlug(username, gameSlug);

        if (!pgOpt.isPresent()) {
            return Optional.empty();
        }

        var pg = pgOpt.get();


        ObjectId profileGameId = new ObjectId(pg.getId());

        PublicProfileGameResponse.Review reviewDto = profileReviewRepository
                .findProjectedByProfileGameId(profileGameId)
                .map(r -> new PublicProfileGameResponse.Review(
                        r.getContent(),
                        r.getSpoiler(),
                        r.getReplay(),
                        r.getLikes(),
                        r.getCreatedAt()
                ))
                .orElse(null);

        // 4) build response
        return Optional.of(new PublicProfileGameResponse(
                pg.getGameId(),
                pg.getGameName(),
                pg.getGameSlug(),
                pg.getGameCover(),
                pg.getRating(),
                pg.getLiked(),
                pg.getHasReview(),
                pg.getStatus(),
                pg.getFavoriteOrder(),
                pg.getCreatedAt(),
                reviewDto
        ));
    }
}

