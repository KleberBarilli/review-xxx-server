package com.idealizer.review_x.application.games.profile.game.usecases;

import com.idealizer.review_x.application.games.profile.game.responses.PublicProfileGameResponse;
import com.idealizer.review_x.domain.activity.comment.entities.Comment;
import com.idealizer.review_x.domain.activity.comment.entities.CommentType;
import com.idealizer.review_x.domain.activity.comment.repositories.CommentRepository;
import com.idealizer.review_x.domain.profile.game.interfaces.SimpleProfileGame;
import com.idealizer.review_x.domain.profile.game.repositories.ProfileGameLogRepository;
import com.idealizer.review_x.domain.profile.game.repositories.ProfileGameRepository;
import com.idealizer.review_x.domain.profile.game.repositories.ProfileReviewRepository;
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
    private final ProfileReviewRepository profileReviewRepository;
    private final CommentRepository commentRepository;
    private final ProfileGameLogRepository profileGameLogRepository;

    public FindProfileGameBySlugAndUsernameUseCase(ProfileGameRepository profileGameRepository,
                                                   ProfileReviewRepository profileReviewRepository,
                                                   CommentRepository commentRepository,
                                                   ProfileGameLogRepository profileGameLogRepository
    ) {
        this.profileGameRepository = profileGameRepository;
        this.profileReviewRepository = profileReviewRepository;
        this.commentRepository = commentRepository;
        this.profileGameLogRepository = profileGameLogRepository;
    }

    public Optional<PublicProfileGameResponse> execute(String gameSlug, String username, Boolean includeComments,
                                                       Boolean includeLog) {
        Optional<SimpleProfileGame> pgOpt = profileGameRepository.findProjectedByUsernameAndGameSlug(username, gameSlug);

        if (!pgOpt.isPresent()) {
            return Optional.empty();
        }

        var pg = pgOpt.get();


        ObjectId profileGameId = new ObjectId(pg.getId());

        PublicProfileGameResponse.Review reviewDto = profileReviewRepository
                .findProjectedByProfileGameId(profileGameId)
                .map(r -> new PublicProfileGameResponse.Review((r.getId()),
                        r.getContent(),
                        r.getSpoiler(),
                        r.getReplay(),
                        r.getLikeCount(),
                        r.getCreatedAt()
                ))
                .orElse(null);

        List<PublicProfileGameResponse.Comment> comments = List.of();
        if (includeComments) {
            var list = commentRepository.findAllByTargetIdAndTargetTypeAndDeletedAtIsNull(
                    new ObjectId(reviewDto.id()), CommentType.REVIEW);
            comments = list.stream().map(
                    c -> new PublicProfileGameResponse.Comment(
                            "c.getUsername() TO DO",
                            c.getContent(),
                            c.getSpoiler(),
                            c.getLikeCount(),
                            c.getCreatedAt(),
                            c.getEditedAt()
                    )).toList();

        }
        List<PublicProfileGameResponse.Log> logs = List.of();

        if (includeLog) {
            logger.log(Level.INFO, "Finding profile game by profileId: " + profileGameId);
            logger.log(Level.INFO, "Finding profile game by userId: " + new ObjectId(pg.getUserId()));
            var list = profileGameLogRepository.findByProfileGameIdAndUserId(profileGameId, new ObjectId(pg.getUserId()));
            logs = list.stream().map(
                    c -> new PublicProfileGameResponse.Log(
                            c.getYear(),
                            c.getMonth(),
                            c.getDay(),
                            c.getMinutesPlayed(),
                            c.getNote()
                    )).toList();

        }


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
                reviewDto,
                comments,
                logs
        ));
    }
}

