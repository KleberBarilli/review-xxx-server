package com.idealizer.review_x.modules.actors.services;

import com.idealizer.review_x.modules.actors.entities.Actor;
import com.idealizer.review_x.modules.actors.repositories.ActorRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class FindActorByIdService {
    private final ActorRepository actorRepository;
    public FindActorByIdService(ActorRepository actorRepository) {
        this.actorRepository = actorRepository;
    }

    public Optional<Actor> execute(UUID id) {
        return actorRepository.findById(id);
    }

}
