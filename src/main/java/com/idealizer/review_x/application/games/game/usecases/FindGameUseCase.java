package com.idealizer.review_x.application.games.game.usecases;

import com.idealizer.review_x.application.games.game.mappers.GameMapper;
import com.idealizer.review_x.application.games.game.responses.FindGameResponse;
import com.idealizer.review_x.application.games.game.responses.SimpleGameResponse;
import com.idealizer.review_x.common.dtos.FindAllGamesDTO;
import com.idealizer.review_x.domain.game.entities.Game;
import com.idealizer.review_x.domain.game.repositories.GameRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Service
public class FindGameUseCase {

    private final MongoTemplate mongoTemplate;
    private final GameMapper gameMapper;

    public FindGameUseCase(MongoTemplate mongoTemplate, GameMapper gameMapper) {
        this.mongoTemplate = mongoTemplate;
        this.gameMapper = gameMapper;
    }

    public FindGameResponse execute(FindAllGamesDTO dto) {
        int size = (dto.limit() <= 0) ? 20 : Math.min(dto.limit(), 50);
        int page = Math.max(0, dto.pageNumber());

        Sort sortOrder = resolveSort(dto.sort(), dto.order());
        Pageable pageable = PageRequest.of(page, size, sortOrder);

        Query q = new Query();

        if (dto.slug() != null && !dto.slug().isBlank()) {
            String s = normalizeSlugQuery(dto.slug());
            q.addCriteria(Criteria.where("slug").gte(s).lt(s + "\uffff"));
        }

        if (dto.developer() != null && !dto.developer().isBlank()) {
            String dev = java.util.regex.Pattern.quote(dto.developer().trim());
            q.addCriteria(Criteria.where("developer").regex("^" + dev + "$", "i"));
        }

        if (dto.genres() != null && !dto.genres().isEmpty()) {
            q.addCriteria(Criteria.where("genres").in(dto.genres()));
        }
        if (dto.modes() != null && !dto.modes().isEmpty()) {
            q.addCriteria(Criteria.where("modes").in(dto.modes()));
        }
        if (dto.status() != null && !dto.status().isEmpty()) {
            q.addCriteria(Criteria.where("status").is(dto.status()));
        }
        if (dto.platforms() != null && !dto.platforms().isEmpty()) {
            q.addCriteria(Criteria.where("platforms").in(dto.platforms()));
        }
        if (dto.types() != null && !dto.types().isEmpty()) {
            q.addCriteria(Criteria.where("type").in(dto.types()));
        }

        if (dto.engines() != null && !dto.engines().isEmpty()) {
            q.addCriteria(Criteria.where("engines").in(dto.engines()));
        }

        if (dto.firstReleaseDate() != null) {
            q.addCriteria(Criteria.where("firstReleaseDate").gte(dto.firstReleaseDate()));
        }

        q.with(pageable);

        List<Game> items = mongoTemplate.find(q, Game.class, "games");
        long total = mongoTemplate.count(Query.of(q).limit(-1).skip(-1).with(Sort.unsorted()), Game.class, "games");

        List<SimpleGameResponse> data = gameMapper.toSimpleDomainList(items);
        int totalPages = (int) Math.ceil(total / (double) size);
        boolean isLast = page >= Math.max(0, totalPages - 1);

        FindGameResponse resp = new FindGameResponse();
        resp.setpageNumber(page);
        resp.setLimit(size);
        resp.setTotalElements(total);
        resp.setTotalPages(totalPages);
        resp.setLast(isLast);
        resp.setData(data);
        return resp;
    }

    private Sort resolveSort(String sort, String order) {
        String sortField = (sort == null || sort.isBlank()) ? "total_rating_count" : sort;
        boolean asc = order != null && order.equalsIgnoreCase(Sort.Direction.ASC.name());
        Sort s = asc ? Sort.by(sortField).ascending() : Sort.by(sortField).descending();
        return s.and(Sort.by("_id").descending()); // sort estável
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
}