package com.idealizer.review_x.application.games.profile.game.log.usecases;

import com.idealizer.review_x.domain.profile.game.repositories.ProfileGameLogRepository;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

@Service
public class RemoveProfileGameLogUseCase {
    private final ProfileGameLogRepository profileGameLogRepository;
    public RemoveProfileGameLogUseCase(ProfileGameLogRepository profileGameLogRepository) {
        this.profileGameLogRepository = profileGameLogRepository;
    }

    public void execute(ObjectId id, ObjectId userId) {
        long deletedCount = profileGameLogRepository.deleteByIdAndUserId(id, userId);
        if (deletedCount == 0) {
            throw new IllegalArgumentException("ProfileGameLog not found");
        }
    }

}
