package com.idealizer.review_x.application.games.profile.game.usecases;

import com.idealizer.review_x.application.games.profile.game.responses.PublicProfileGameDetailedResponse;
import com.idealizer.review_x.domain.core.LogID;
import com.idealizer.review_x.domain.core.activity.comment.entities.CommentType;
import com.idealizer.review_x.domain.core.activity.comment.repositories.CommentRepository;
import com.idealizer.review_x.domain.core.profile.game.interfaces.SimpleProfileGame;
import com.idealizer.review_x.domain.core.profile.game.repositories.ProfileGameLogRepository;
import com.idealizer.review_x.domain.core.profile.game.repositories.ProfileGameRepository;
import com.idealizer.review_x.domain.core.review.repositories.ReviewRepository;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;


@Service
public class FindProfileGameBySlugAndUsernameUseCase {
    private final Logger logger = Logger.getLogger(FindProfileGameBySlugAndUsernameUseCase.class.getName());
    //todocache
    private final ProfileGameRepository profileGameRepository;
    private final ReviewRepository reviewRepository;
    private final CommentRepository commentRepository;
    private final ProfileGameLogRepository profileGameLogRepository;

    public FindProfileGameBySlugAndUsernameUseCase(ProfileGameRepository profileGameRepository,
                                                   ReviewRepository reviewRepository,
                                                   CommentRepository commentRepository,
                                                   ProfileGameLogRepository profileGameLogRepository
    ) {
        this.profileGameRepository = profileGameRepository;
        this.reviewRepository = reviewRepository;
        this.commentRepository = commentRepository;
        this.profileGameLogRepository = profileGameLogRepository;
    }

    public Optional<PublicProfileGameDetailedResponse> execute(String gameSlug, String username, Boolean includeComments,
                                                               Boolean includeLog) {
        Optional<SimpleProfileGame> pgOpt = profileGameRepository.findProjectedByUsernameAndGameSlug(username, gameSlug);

        if (!pgOpt.isPresent()) {
            return Optional.empty();
        }

        var pg = pgOpt.get();


        ObjectId profileGameId = new ObjectId(pg.getId());

        PublicProfileGameDetailedResponse.Review reviewDto = reviewRepository
                .findProjectedByProfileTargetIdAndTargetType(profileGameId, LogID.GAMES)
                .map(r -> new PublicProfileGameDetailedResponse.Review((r.getId()),
                        r.getContent(),
                        r.getSpoiler(),
                        r.getReplay(),
                        r.getLikeCount(),
                        r.getCreatedAt()
                ))
                .orElse(null);

        List<PublicProfileGameDetailedResponse.Comment> comments = List.of();
        if (includeComments) {
            var list = commentRepository.findAllByTargetIdAndTargetTypeAndDeletedAtIsNull(
                    new ObjectId(reviewDto.id()), CommentType.REVIEW);
            comments = list.stream().map(
                    c -> new PublicProfileGameDetailedResponse.Comment(
                            c.getUsername(),
                            c.getFullName(),
                            c.getContent(),
                            c.getSpoiler(),
                            c.getLikeCount(),
                            c.getCreatedAt(),
                            c.getEditedAt()
                    )).toList();

        }
        List<PublicProfileGameDetailedResponse.Log> logs = List.of();

        if (includeLog) {
            logger.log(Level.INFO, "Finding profile game by profileId: " + profileGameId);
            logger.log(Level.INFO, "Finding profile game by userId: " + new ObjectId(pg.getUserId()));
            var list = profileGameLogRepository.findByProfileGameIdAndUserId(profileGameId, new ObjectId(pg.getUserId()));
            logs = list.stream().map(
                    c -> new PublicProfileGameDetailedResponse.Log(
                            c.getYear(),
                            c.getMonth(),
                            c.getDay(),
                            c.getMinutesPlayed(),
                            c.getNote()
                    )).toList();

        }


        return Optional.of(new PublicProfileGameDetailedResponse(
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
                reviewDto,
                comments,
                logs
        ));
    }
}

