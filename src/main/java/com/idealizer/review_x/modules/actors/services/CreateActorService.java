package com.idealizer.review_x.modules.actors.services;

import com.idealizer.review_x.modules.actors.entities.Actor;
import com.idealizer.review_x.modules.actors.repositories.ActorRepository;
import org.springframework.stereotype.Service;

@Service
public class CreateActorService {
    private final ActorRepository actorRepository;

    public CreateActorService(ActorRepository actorRepository) {
        this.actorRepository = actorRepository;
    }

    public void execute(Actor actor) {
         actorRepository.save(actor);
    }
}
