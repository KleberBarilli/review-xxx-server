package com.idealizer.review_x.actors.services;

import com.idealizer.review_x.Person;
import com.idealizer.review_x.actors.entities.ActorEntity;
import com.idealizer.review_x.actors.repositories.ActorRepository;
import org.springframework.stereotype.Service;

@Service
public class CreateActorService {


    private final ActorRepository actorRepository;

    public CreateActorService(ActorRepository actorRepository) {
        this.actorRepository = actorRepository;
    }

    public ActorEntity execute(ActorEntity actor) {

        ActorEntity actorDto = new ActorEntity();
        Person person = new Person();

        person.setName(actor.getPerson().getName());
        person.setDescription(actor.getPerson().getDescription());
        person.setAvatarUrl(actor.getPerson().getAvatarUrl());
        person.setBirthDate(actor.getPerson().getBirthDate());


        actorDto.setPerson(person);
        return actorRepository.save(actorDto);
    }
}
