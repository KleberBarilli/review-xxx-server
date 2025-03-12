package com.idealizer.review_x.modules.actors.services;

import com.idealizer.review_x.modules.actors.entities.Actor;
import com.idealizer.review_x.modules.actors.repositories.ActorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FindActorService {
    private final ActorRepository actorRepository;
    public FindActorService( ActorRepository actorRepository) {
        this.actorRepository = actorRepository;
    }

    public List<Actor> execute(String name, String nationality){
        return this.actorRepository.findByNameAndNationality(name, nationality);
    }
}
