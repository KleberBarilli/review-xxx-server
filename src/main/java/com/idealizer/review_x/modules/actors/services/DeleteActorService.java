package com.idealizer.review_x.modules.actors.services;

import com.idealizer.review_x.modules.actors.repositories.ActorRepository;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

@Service
public class DeleteActorService {
    private final ActorRepository actorRepository;

    public DeleteActorService(ActorRepository actorRepository) {
        this.actorRepository = actorRepository;
    }

    public void execute(ObjectId id) {
        this.actorRepository.deleteById(id);
    }
}
