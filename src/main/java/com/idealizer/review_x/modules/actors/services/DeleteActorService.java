package com.idealizer.review_x.modules.actors.services;

import com.idealizer.review_x.modules.actors.repositories.ActorRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class DeleteActorService {
    private final ActorRepository actorRepository;

    public DeleteActorService(ActorRepository actorRepository) {
        this.actorRepository = actorRepository;
    }

    public void execute(UUID id) {
        this.actorRepository.deleteById(id);
    }
}
