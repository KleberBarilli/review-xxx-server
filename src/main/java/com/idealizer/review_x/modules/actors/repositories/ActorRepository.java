package com.idealizer.review_x.modules.actors.repositories;

import com.idealizer.review_x.modules.actors.entities.Actor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActorRepository extends JpaRepository<Actor, Long> {
}
