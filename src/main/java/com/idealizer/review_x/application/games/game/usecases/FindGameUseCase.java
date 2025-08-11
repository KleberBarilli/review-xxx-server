package com.idealizer.review_x.application.games.game.usecases;

import com.idealizer.review_x.application.games.game.mappers.GameMapper;
import com.idealizer.review_x.application.games.game.responses.FindGameResponse;
import com.idealizer.review_x.application.games.game.responses.SimpleGameResponse;
import com.idealizer.review_x.domain.game.entities.Game;
import com.idealizer.review_x.domain.game.repositories.GameRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FindGameUseCase {
    private final GameRepository gameRepository;
    private final GameMapper gameMapper;

    public FindGameUseCase(GameRepository gameRepository, GameMapper gameMapper) {
        this.gameRepository = gameRepository;
        this.gameMapper = gameMapper;
    }

    public FindGameResponse execute(int limit, int pageNumber, String sort, String order, String slug) {

        // sanitize paging
        int size = (limit <= 0) ? 20 : limit;
        int page = (pageNumber < 0) ? 0 : pageNumber;

        // 1) If no slug -> keep your old pageable flow (honors sort/order)
        if (slug == null || slug.isBlank()) {
            Sort sortOrder = order != null && order.equalsIgnoreCase(Sort.Direction.ASC.name())
                    ? Sort.by(sort).ascending()
                    : Sort.by(sort).descending();

            Pageable pageable = PageRequest.of(page, size, sortOrder);
            Page<Game> games = gameRepository.findAll(pageable);

            return toResponse(games.getContent(), games.getTotalElements(), page, size);
        }

        // 2) With slug -> use Atlas Search autocomplete (popularity desc + relevance)
        String q = slug.trim();
        int skip = page * size;

        // results page
        List<Game> items = gameRepository.autocompleteSlug(q, skip, size);

        // total hits (for pagination)
        long total = gameRepository.autocompleteSlugCount(q)
                .stream()
                .findFirst()
                .map(GameRepository.TotalOnly::getTotal)
                .orElse(0L);

        return toResponse(items, total, page, size);
    }

    private FindGameResponse toResponse(List<Game> games, long total, int page, int size) {
        List<SimpleGameResponse> data = gameMapper.toSimpleDomainList(games);

        int totalPages = (int) Math.ceil(total / (double) size);
        boolean isLast = page >= Math.max(0, totalPages - 1);

        FindGameResponse response = new FindGameResponse();
        response.setpageNumber(page);
        response.setLimit(size);
        response.setTotalElements(total);
        response.setTotalPages(totalPages);
        response.setLast(isLast);
        response.setData(data);
        return response;
    }
}