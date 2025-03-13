package com.idealizer.review_x.modules.actors.validators;

import com.idealizer.review_x.modules.actors.entities.Actor;
import com.idealizer.review_x.modules.actors.repositories.ActorRepository;
import org.springframework.stereotype.Component;

@Component

public class ActorValidator {
    private ActorRepository actorRepository;

    public ActorValidator(ActorRepository actorRepository) {
        this.actorRepository = actorRepository;
    }

    public void validate (Actor actor) {


    }
}
