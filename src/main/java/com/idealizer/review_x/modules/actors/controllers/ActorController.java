package com.idealizer.review_x.modules.actors.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.idealizer.review_x.modules.actors.controllers.dto.ActorDTO;
import com.idealizer.review_x.modules.actors.entities.Actor;
import com.idealizer.review_x.modules.actors.services.*;
import jakarta.validation.Valid;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/actors")
public class ActorController {


    private CreateActorService createActorService;
    private UpdateActorService updateActorService;
    private FindActorByIdService findActorByIdService;
    private FindActorService findActorService;
    private DeleteActorService deleteActorService;

    public ActorController(
            CreateActorService createActorService,
            UpdateActorService updateActorService,
            FindActorByIdService findActorByIdService,
            FindActorService findActorService,
            DeleteActorService deleteActorService) {
        this.createActorService = createActorService;
        this.updateActorService = updateActorService;
        this.findActorByIdService = findActorByIdService;
        this.findActorService = findActorService;
        this.deleteActorService = deleteActorService;
    }

    @PostMapping
    public ResponseEntity<Void> createActor (@RequestBody @Valid ActorDTO actor) throws JsonProcessingException {

        Actor actorEntity = actor.toEntity();

        this.createActorService.execute(actorEntity);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(actorEntity.getId()).toUri();
        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateActor(@PathVariable("id") String id, @RequestBody @Valid ActorDTO dto) {

        var actorId = UUID.fromString(id);

        Optional<Actor> actorOptional = this.findActorByIdService.execute(actorId);

        if(actorOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var actor = actorOptional.get();
        actor.getPerson().setName(dto.name());
        actor.getPerson().setBirthDate(dto.birthDate());
        actor.getPerson().setAvatarUrl(dto.avatarUrl());
        actor.getPerson().setDescription(dto.description());
        actor.getPerson().setNationality(dto.nationality());



        this.updateActorService.execute(actor);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public  ResponseEntity<ActorDTO> findById(@PathVariable("id") String id) {
        Optional<Actor> actorOptional = this.findActorByIdService.execute(UUID.fromString(id));
        if (actorOptional.isPresent()) {
            Actor actor = actorOptional.get();
            ActorDTO dto = new ActorDTO(actor.getId(), actor.getPerson().getName(), actor.getPerson().getBirthDate(),
                    actor.getPerson().getAvatarUrl(), actor.getPerson().getDescription(), actor.getPerson().getNationality(),
                    actor.getPerson().getCreatedAt(), actor.getPerson().getUpdatedAt());
            return ResponseEntity.ok(dto);
        }
    return ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<ActorDTO>> find(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "nationality", required = false) String nationality) {


        List<Actor> result =  this.findActorService.execute(name, nationality);
        List<ActorDTO> dtos = result.stream().map(actor -> new ActorDTO(actor.getId(),
                actor.getPerson().getName(),
                actor.getPerson().getBirthDate(),
                actor.getPerson().getAvatarUrl(),
                actor.getPerson().getDescription(),
                actor.getPerson().getNationality(),
                actor.getPerson().getCreatedAt(),
                actor.getPerson().getUpdatedAt())).collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") String id) {
        this.deleteActorService.execute(UUID.fromString(id));
        return ResponseEntity.noContent().build();
    }

}
