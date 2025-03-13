package com.idealizer.review_x.modules.actors.services;

import com.idealizer.review_x.modules.actors.entities.Actor;
import com.idealizer.review_x.modules.actors.repositories.ActorRepository;
import com.idealizer.review_x.modules.actors.validators.ActorValidator;
import org.springframework.stereotype.Service;

@Service
public class CreateActorService {
    private final ActorRepository actorRepository;
    private final ActorValidator actorValidator;

    public CreateActorService(ActorRepository actorRepository, ActorValidator actorValidator) {
        this.actorRepository = actorRepository;
        this.actorValidator = actorValidator;
    }

    public void execute(Actor actor) {
         this.actorValidator.validate(actor);
         actorRepository.save(actor);
    }
}
