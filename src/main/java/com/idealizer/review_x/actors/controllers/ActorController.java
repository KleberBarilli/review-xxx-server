package com.idealizer.review_x.actors.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.idealizer.review_x.Person;
import com.idealizer.review_x.actors.entities.ActorEntity;
import com.idealizer.review_x.actors.services.CreateActorService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/actors")
public class ActorController {


    private CreateActorService createActorService;

    public ActorController(CreateActorService createActorService) {
        this.createActorService = createActorService;
    }

    @PostMapping
    public ActorEntity createActor (@RequestBody ActorEntity actor) throws JsonProcessingException {

        return this.createActorService.execute(actor);
    }


}
