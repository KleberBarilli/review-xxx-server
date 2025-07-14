package com.idealizer.review_x.modules.actors.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.idealizer.review_x.common.controller.GenericController;
import com.idealizer.review_x.modules.actors.controllers.dto.ActorDTO;
import com.idealizer.review_x.modules.actors.controllers.mappers.ActorMapper;
import com.idealizer.review_x.modules.actors.entities.Actor;
import com.idealizer.review_x.modules.actors.services.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/actors")
@Tag(name = "Actors")
public class ActorController implements GenericController {

    private static final Logger logger = LoggerFactory.getLogger(ActorController.class);

    private CreateActorService createActorService;
    private UpdateActorService updateActorService;
    private FindActorByIdService findActorByIdService;
    private FindActorService findActorService;
    private DeleteActorService deleteActorService;

    private final ActorMapper actorMapper;

    public ActorController(
            CreateActorService createActorService,
            UpdateActorService updateActorService,
            FindActorByIdService findActorByIdService,
            FindActorService findActorService,
            DeleteActorService deleteActorService, ActorMapper actorMapper) {
        this.createActorService = createActorService;
        this.updateActorService = updateActorService;
        this.findActorByIdService = findActorByIdService;
        this.findActorService = findActorService;
        this.deleteActorService = deleteActorService;
        this.actorMapper = actorMapper;
    }

    @PostMapping
    @Operation(summary = "Save a new actor", description = "Create a new actor")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Actor created successfully"),
    })
    public ResponseEntity<Void> createActor(@RequestBody @Valid ActorDTO dto) throws JsonProcessingException {
        logger.info("Creating a new actor: {}", dto);
        Actor actor = actorMapper.toEntity(dto);
        this.createActorService.execute(actor);
        URI location = getLocationHeader(actor.getId());
        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an actor")
    public ResponseEntity<Void> updateActor(@PathVariable("id") String id, @RequestBody @Valid ActorDTO dto) {
        logger.info("Updating actor with id: {}", id);
        ObjectId actorId = new ObjectId(id);
        Optional<Actor> actorOptional = this.findActorByIdService.execute(actorId);
        if (actorOptional.isEmpty()) {
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
    @Operation(summary = "Find an actor by id")
    public ResponseEntity<ActorDTO> findById(@PathVariable("id") String id) {
        ObjectId actorId = new ObjectId(id);
        logger.info("Finding actor by id: {}", actorId);
        return findActorByIdService
                .execute(actorId)
                .map(actor -> {
                    ActorDTO dto = actorMapper.toDTO(actor);
                    return ResponseEntity.ok(dto);
                }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    @Operation(summary = "Find actors")
    public ResponseEntity<List<ActorDTO>> find(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "nationality", required = false) String nationality) {

        List<Actor> result = this.findActorService.execute(name, nationality);
        List<ActorDTO> dtos = result.stream()
                .map(actorMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an actor")
    public ResponseEntity<Void> delete(@PathVariable("id") String id) {
        ObjectId actorId = new ObjectId(id);
        logger.info("Deleting actor with id: {}", id);
        this.deleteActorService.execute(actorId);
        return ResponseEntity.noContent().build();
    }

}
