package com.idealizer.review_x.actors.repositories;

import com.idealizer.review_x.actors.entities.ActorEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActorRepository extends JpaRepository<ActorEntity, Long> {
}
