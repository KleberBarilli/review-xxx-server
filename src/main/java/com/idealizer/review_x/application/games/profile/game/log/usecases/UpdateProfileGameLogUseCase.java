package com.idealizer.review_x.application.games.profile.game.log.usecases;

import com.idealizer.review_x.common.dtos.profile.game.UpsertProfileGameLogDTO;
import com.idealizer.review_x.domain.profile.game.entities.ProfileGameLog;
import com.idealizer.review_x.domain.profile.game.repositories.ProfileGameLogRepository;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

@Service
public class UpdateProfileGameLogUseCase {
    private final ProfileGameLogRepository profileGameLogRepository;
    public UpdateProfileGameLogUseCase(ProfileGameLogRepository profileGameLogRepository) {
        this.profileGameLogRepository = profileGameLogRepository;
    }
    public void execute(ObjectId id, UpsertProfileGameLogDTO dto, ObjectId userId) {
        ProfileGameLog profileGameLog = profileGameLogRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new IllegalArgumentException("ProfileGameLog not found"));

        profileGameLog.setYear(dto.year());
        profileGameLog.setMonth(dto.month());
        profileGameLog.setDay(dto.day());
        profileGameLog.setMinutesPlayed(dto.minutesPlayed());
        profileGameLog.setNote(dto.note());
        profileGameLogRepository.save(profileGameLog);

    }
}
