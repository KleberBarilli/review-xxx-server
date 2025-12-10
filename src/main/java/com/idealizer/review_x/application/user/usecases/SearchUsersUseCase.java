package com.idealizer.review_x.application.user.usecases;

import com.idealizer.review_x.application.user.responses.SearchUserResponse;
import com.idealizer.review_x.domain.core.user.entities.User;
import com.idealizer.review_x.domain.core.user.repositories.UserRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchUsersUseCase {

    private final UserRepository userRepository;

    public SearchUsersUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<SearchUserResponse> execute(String query) {
        if (query == null || query.isBlank()) {
            return List.of();
        }

        List<User> users = userRepository.searchByTerm(query.trim(), PageRequest.of(0, 10));

        return users.stream()
                .map(user -> new SearchUserResponse(
                        user.getId().toHexString(),
                        user.getName(),
                        user.getFullName(),
                        user.getAvatarUrl(),
                        user.getBio()
                ))
                .toList();
    }
}