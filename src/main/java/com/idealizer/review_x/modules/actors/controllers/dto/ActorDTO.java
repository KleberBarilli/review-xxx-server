package com.idealizer.review_x.modules.actors.controllers.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.idealizer.review_x.Person;
import com.idealizer.review_x.modules.actors.entities.Actor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record ActorDTO(UUID id, String name,
                       @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
                       LocalDate birthDate,
                       String avatarUrl,
                       String description,
                       String nationality, LocalDateTime createdAt, LocalDateTime updatedAt) {


    public Actor toEntity() {
        Actor actor = new Actor();
        Person person = new Person();
        person.setName(this.name);
        person.setBirthDate(this.birthDate);
        person.setAvatarUrl(this.avatarUrl);
        person.setDescription(this.description);
        person.setNationality(this.nationality);

        actor.setPerson(person);

        return actor;
    }

}


