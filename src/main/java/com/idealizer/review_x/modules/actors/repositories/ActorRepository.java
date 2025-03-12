package com.idealizer.review_x.modules.actors.repositories;

import com.idealizer.review_x.modules.actors.entities.Actor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface ActorRepository extends JpaRepository<Actor, UUID> {


    @Query("SELECT a FROM Actor a WHERE a.person.name LIKE %:name% AND a.person.nationality LIKE %:nationality%")
    List<Actor> findByNameAndNationality(@Param("name") String name, @Param("nationality") String nationality);
}
