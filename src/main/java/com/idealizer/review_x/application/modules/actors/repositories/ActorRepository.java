package com.idealizer.review_x.application.modules.actors.repositories;

import com.idealizer.review_x.application.modules.actors.entities.Actor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;


import java.util.List;


public interface ActorRepository extends MongoRepository<Actor, ObjectId> {

    List<Actor> findByPersonNameContainingIgnoreCaseAndPersonNationalityContainingIgnoreCase(String name, String nationality);

}
