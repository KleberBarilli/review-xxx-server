package com.idealizer.review_x.application.games.profile.game.usecases;

import com.idealizer.review_x.application.games.profile.game.responses.PublicProfileGameDetailedResponse;
import com.idealizer.review_x.domain.core.LogID;
import com.idealizer.review_x.domain.core.activity.comment.entities.CommentType;
import com.idealizer.review_x.domain.core.activity.comment.repositories.CommentRepository;
import com.idealizer.review_x.domain.core.activity.like.repositories.LikeRepository;
import com.idealizer.review_x.domain.core.profile.game.interfaces.SimpleProfileGame;
import com.idealizer.review_x.domain.core.profile.game.repositories.ProfileGameRepository;
import com.idealizer.review_x.domain.core.review.repositories.ReviewRepository;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;


@Service
public class FindProfileGameBySlugAndUsernameUseCase {
    private final Logger logger = Logger.getLogger(FindProfileGameBySlugAndUsernameUseCase.class.getName());
    //todocache
    private final ProfileGameRepository profileGameRepository;
    private final ReviewRepository reviewRepository;
    private final CommentRepository commentRepository;
    private final LikeRepository likeRepository;

    public FindProfileGameBySlugAndUsernameUseCase(ProfileGameRepository profileGameRepository,
                                                   ReviewRepository reviewRepository,
                                                   CommentRepository commentRepository,
                                                   LikeRepository likeRepository
    ) {
        this.profileGameRepository = profileGameRepository;
        this.reviewRepository = reviewRepository;
        this.commentRepository = commentRepository;
        this.likeRepository = likeRepository;
    }

    public Optional<PublicProfileGameDetailedResponse> execute(String gameSlug, String username, Boolean includeComments, ObjectId userLoggedId) {

        Optional<SimpleProfileGame> pgOpt = profileGameRepository.findProjectedByUsernameAndGameSlug(username, gameSlug);
        if (!pgOpt.isPresent()) {
            return Optional.empty();
        }
        var pg = pgOpt.get();
        ObjectId profileGameId = new ObjectId(pg.getId());

        var reviewProjOpt = reviewRepository.findProjectedByProfileTargetIdAndTargetType(profileGameId, LogID.GAMES);

        PublicProfileGameDetailedResponse.Review reviewDto = null;
        List<PublicProfileGameDetailedResponse.Comment> comments = List.of();

        if (reviewProjOpt.isPresent()) {
            var r = reviewProjOpt.get();
            String reviewIdStr = r.getId();
            ObjectId reviewIdObj = new ObjectId(reviewIdStr);

            boolean isLikedByCurrentUser = false;
            if (userLoggedId != null) {
                isLikedByCurrentUser = this.likeRepository.existsByTargetIdAndUserId(reviewIdObj, userLoggedId);
            }

            reviewDto = new PublicProfileGameDetailedResponse.Review(
                    reviewIdStr,
                    r.getContent(),
                    r.getSpoiler(),
                    r.getReplay(),
                    r.getLikeCount(),
                    isLikedByCurrentUser,
                    r.getCreatedAt()
            );

            if (Boolean.TRUE.equals(includeComments)) {
                var list = commentRepository.findAllByTargetIdAndTargetTypeAndDeletedAtIsNull(
                        reviewIdObj, CommentType.REVIEW);

                comments = list.stream().map(c -> new PublicProfileGameDetailedResponse.Comment(
                        c.getUsername(),
                        c.getFullName(),
                        c.getContent(),
                        c.getSpoiler(),
                        c.getLikeCount(),
                        c.getCreatedAt(),
                        c.getEditedAt()
                )).toList();
            }
        }

        return Optional.of(new PublicProfileGameDetailedResponse(
                pg.getGameId(),
                pg.getGameName(),
                pg.getGameSlug(),
                pg.getGameCover(),
                pg.getRating(),
                pg.getLiked(),
                pg.getMastered(),
                pg.getOwned(),
                pg.getBacklog(),
                pg.getPlaying(),
                pg.getWishlist(),
                pg.getPlayedOn(),
                pg.getPlaytimeMinutes(),
                pg.getSourcePlatform(),
                pg.getHasReview(),
                pg.getStartedAt(),
                pg.getFinishedAt(),
                pg.getStatus(),
                pg.getFavoriteOrder(),
                pg.getCreatedAt(),
                reviewDto, // Pode ser null se não tiver review, conforme seu design original
                comments
        ));
    }
}

