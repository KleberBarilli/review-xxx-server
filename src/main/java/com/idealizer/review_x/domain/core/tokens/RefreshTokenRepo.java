package com.idealizer.review_x.domain.core.tokens;

import org.springframework.data.repository.CrudRepository;
public interface RefreshTokenRepo extends CrudRepository<RefreshToken, String> {}
