package com.idealizer.review_x.application.games.profile.game.usecases;

import com.idealizer.review_x.common.dtos.PageResponse;
import com.idealizer.review_x.common.dtos.profile.game.FindProfileGamesDTO;
import com.idealizer.review_x.domain.core.profile.game.interfaces.BaseProfileGame;
import com.idealizer.review_x.domain.core.profile.game.repositories.ProfileGameRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;


@Service
public class FindProfileGameByUsernameUseCase {
    private final ProfileGameRepository profileGameRepository;

    public FindProfileGameByUsernameUseCase(
            ProfileGameRepository profileGameRepository
    ) {
        this.profileGameRepository = profileGameRepository;
    }

    public PageResponse<BaseProfileGame> execute(String username, FindProfileGamesDTO f) {
        int page = Math.max(f.pageNumber(), 0);
        int size = Math.max(f.limit(), 1);

        Page<BaseProfileGame> p = profileGameRepository.searchProjected(
                username, f, PageRequest.of(page, size)
        );

        return new PageResponse<>(
                page,
                size,
                p.getTotalElements(),
                p.getTotalPages(),
                p.isLast(),
                p.getContent()
        );
    }
}
