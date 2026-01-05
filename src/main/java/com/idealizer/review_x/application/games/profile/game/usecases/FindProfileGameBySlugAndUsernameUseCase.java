package com.idealizer.review_x.application.games.profile.game.usecases;

import com.idealizer.review_x.application.games.profile.game.responses.PublicProfileGameDetailedResponse;
import com.idealizer.review_x.domain.core.LogID;
import com.idealizer.review_x.domain.core.activity.comment.entities.Comment;
import com.idealizer.review_x.domain.core.activity.comment.entities.CommentType;
import com.idealizer.review_x.domain.core.activity.comment.repositories.CommentRepository;
import com.idealizer.review_x.domain.core.activity.like.repositories.LikeRepository;
import com.idealizer.review_x.domain.core.profile.game.interfaces.SimpleProfileGame;
import com.idealizer.review_x.domain.core.profile.game.repositories.ProfileGameRepository;
import com.idealizer.review_x.domain.core.review.repositories.ReviewRepository;
import com.idealizer.review_x.domain.core.user.entities.User;
import com.idealizer.review_x.domain.core.user.repositories.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class FindProfileGameBySlugAndUsernameUseCase {

    private final ProfileGameRepository profileGameRepository;
    private final ReviewRepository reviewRepository;
    private final CommentRepository commentRepository;
    private final LikeRepository likeRepository;
    private final UserRepository userRepository;

    public FindProfileGameBySlugAndUsernameUseCase(
            ProfileGameRepository profileGameRepository,
            ReviewRepository reviewRepository,
            CommentRepository commentRepository,
            LikeRepository likeRepository,
            UserRepository userRepository
    ) {
        this.profileGameRepository = profileGameRepository;
        this.reviewRepository = reviewRepository;
        this.commentRepository = commentRepository;
        this.likeRepository = likeRepository;
        this.userRepository = userRepository;
    }

    public Optional<PublicProfileGameDetailedResponse> execute(String gameSlug, String username, Boolean includeComments, ObjectId userLoggedId) {

        Optional<SimpleProfileGame> pgOpt = profileGameRepository.findProjectedByUsernameAndGameSlug(username, gameSlug);
        if (pgOpt.isEmpty()) {
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
                List<Comment> rawComments = commentRepository.findTop20ByTargetIdAndTargetTypeAndDeletedAtIsNullOrderByCreatedAtDesc(
                        reviewIdObj, CommentType.REVIEW);

                if (!rawComments.isEmpty()) {
                    Set<ObjectId> userIds = rawComments.stream()
                            .map(Comment::getUserId)
                            .filter(Objects::nonNull)
                            .collect(Collectors.toSet());

                    Map<ObjectId, User> userMap = userRepository.findFromListOptimized(new ArrayList<>(userIds))
                            .stream()
                            .collect(Collectors.toMap(User::getId, Function.identity()));

                    comments = rawComments.stream().map(c -> {
                        User author = userMap.get(c.getUserId());

                        String authorUsername = (author != null) ? author.getName() : "Unknown User";
                        String authorFullName = (author != null) ? author.getFullName() : "";
                        String authorAvatar = (author != null) ? author.getAvatarUrl() : null;

                        return new PublicProfileGameDetailedResponse.Comment(
                                c.getId().toString(),
                                c.getContent(),
                                authorUsername,
                                authorFullName,
                                authorAvatar,
                                c.getSpoiler(),
                                c.getLikeCount(),
                                c.getCreatedAt(),
                                c.getEditedAt()
                        );
                    }).toList();
                }
            }
        }

        return Optional.of(new PublicProfileGameDetailedResponse(
                pg.getId(),
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
                reviewDto,
                comments
        ));
    }
}