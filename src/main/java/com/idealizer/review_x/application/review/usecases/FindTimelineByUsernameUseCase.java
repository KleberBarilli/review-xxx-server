package com.idealizer.review_x.application.review.usecases;

import com.idealizer.review_x.common.dtos.FindTimelineDTO;
import com.idealizer.review_x.common.dtos.PageResponse;
import com.idealizer.review_x.domain.core.profile.BaseTimelineItem;
import com.idealizer.review_x.domain.core.profile.game.repositories.ProfileGameRepository;
import com.idealizer.review_x.domain.core.review.repositories.ReviewRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class FindTimelineByUsernameUseCase {
    private final ProfileGameRepository profileGameRepository;

    public FindTimelineByUsernameUseCase(ProfileGameRepository profileGameRepository) {
        this.profileGameRepository = profileGameRepository;
    }

    public PageResponse<BaseTimelineItem> execute(String username, FindTimelineDTO f) {
        int page = Math.max(f.pageNumber(), 0);
        int size = Math.max(f.limit(), 1);

        PageRequest pageRequest = PageRequest.of(page, size);

        Page<BaseTimelineItem> t = profileGameRepository.timeline(username, f, pageRequest);

        return new PageResponse<>(
                page,
                size,
                t.getTotalElements(),
                t.getTotalPages(),
                t.isLast(),
                t.getContent()
        );
    }
}
