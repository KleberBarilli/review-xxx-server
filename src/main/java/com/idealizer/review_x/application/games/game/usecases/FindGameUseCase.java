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
import java.util.Optional;
import java.util.logging.Logger;

@Service
public class FindGameUseCase {
    private final Logger logger = Logger.getLogger(FindGameUseCase.class.getName());
    private final GameRepository gameRepository;
    private final GameMapper gameMapper;

    public FindGameUseCase(GameRepository gameRepository, GameMapper gameMapper) {
        this.gameRepository = gameRepository;
        this.gameMapper = gameMapper;
    }

    public FindGameResponse execute(int limit, int pageNumber, String sort, String order, String slug) {
        int size = (limit <= 0) ? 20 : limit;
        int page = (pageNumber < 0) ? 0 : pageNumber;

        if (slug == null || slug.isBlank()) {
            Sort sortOrder = resolveSort(sort, order);
            Pageable pageable = PageRequest.of(page, size, sortOrder);
            Page<Game> games = gameRepository.findAll(pageable);
            return toResponse(games.getContent(), games.getTotalElements(), page, size);
        }

        String q = normalizeSlugQuery(slug);

        String pattern = "^" + escapeRegex(q) + "(?:-|$)";

        Sort sortOrder = resolveSort(sort, order);
        Pageable pageable = PageRequest.of(page, size, sortOrder);

        logger.info("Searching for games with slug pattern: " + pattern);
        Page<Game> pageResult = gameRepository.findBySlugRegex(pattern, pageable);
        return toResponse(pageResult.getContent(), pageResult.getTotalElements(), page, size);
    }


    private Sort resolveSort(String sort, String order) {

        String sortField = (sort == null || sort.isBlank()) ? "total_rating_count" : sort;
        boolean asc = order != null && order.equalsIgnoreCase(Sort.Direction.ASC.name());
        Sort s = asc ? Sort.by(sortField).ascending() : Sort.by(sortField).descending();
        return s.and(Sort.by("_id").descending());
    }

    private static String normalizeSlugQuery(String raw) {
        String q = raw.trim().toLowerCase();
        q = q.replaceAll("\\s+", "-");
        q = q.replaceAll("[^a-z0-9-]", "-");
        q = q.replaceAll("-{2,}", "-");
        if (q.startsWith("-")) q = q.substring(1);
        if (q.endsWith("-")) q = q.substring(0, q.length()-1);
        return q;
    }

    private static String escapeRegex(String s) {
        return s.replaceAll("([.^$|()\\[\\]{}*+?\\\\])", "\\\\$1");
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