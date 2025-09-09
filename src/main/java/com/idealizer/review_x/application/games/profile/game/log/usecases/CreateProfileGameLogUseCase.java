package com.idealizer.review_x.application.games.profile.game.log.usecases;

import com.idealizer.review_x.common.dtos.profile.game.UpsertProfileGameLogDTO;
import com.idealizer.review_x.domain.core.profile.game.entities.ProfileGameLog;
import com.idealizer.review_x.domain.core.profile.game.repositories.ProfileGameLogRepository;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

@Service
public class CreateProfileGameLogUseCase {
    private final ProfileGameLogRepository profileGameLogRepository;

    public CreateProfileGameLogUseCase(ProfileGameLogRepository profileGameLogRepository) {
        this.profileGameLogRepository = profileGameLogRepository;
    }

    public void execute(UpsertProfileGameLogDTO dto, ObjectId userId) {
        ProfileGameLog profileGameLog = new ProfileGameLog();
        profileGameLog.setUserId(userId);
        profileGameLog.setProfileGameId(new ObjectId(dto.profileGameId()));
        profileGameLog.setYear(dto.year());
        profileGameLog.setMonth(dto.month());
        profileGameLog.setDay(dto.day());
        profileGameLog.setMinutesPlayed(dto.minutesPlayed());
        profileGameLog.setNote(dto.note());
        profileGameLogRepository.insert(profileGameLog);
    }
}
