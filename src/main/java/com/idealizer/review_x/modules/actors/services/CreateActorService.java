package com.idealizer.review_x.modules.actors.services;

import com.idealizer.review_x.Person;
import com.idealizer.review_x.modules.actors.entities.Actor;
import com.idealizer.review_x.modules.actors.repositories.ActorRepository;
import org.springframework.stereotype.Service;

@Service
public class CreateActorService {


    private final ActorRepository actorRepository;

    public CreateActorService(ActorRepository actorRepository) {
        this.actorRepository = actorRepository;
    }

    public Actor execute(Actor actor) {

        Actor actorDto = new Actor();
        Person person = new Person();

        person.setName(actor.getPerson().getName());
        person.setDescription(actor.getPerson().getDescription());
        person.setAvatarUrl(actor.getPerson().getAvatarUrl());
        person.setBirthDate(actor.getPerson().getBirthDate());
        person.setNationality(actor.getPerson().getNationality());


        actorDto.setPerson(person);
        return actorRepository.save(actorDto);
    }
}
