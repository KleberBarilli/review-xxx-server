package com.idealizer.review_x.domain.core.profile.game.repositories;

import com.idealizer.review_x.common.dtos.FindTimelineDTO;
import com.idealizer.review_x.common.dtos.profile.game.FindProfileGamesDTO;
import com.idealizer.review_x.domain.core.profile.BaseTimelineItem;
import com.idealizer.review_x.domain.core.profile.game.interfaces.BaseProfileGame;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface ProfileGameRepositoryCustom {
    Page<BaseProfileGame> searchProjected(String username, FindProfileGamesDTO f, Pageable pageable);
    Page<BaseTimelineItem> timeline(String username, FindTimelineDTO f, Pageable pageable);
}
