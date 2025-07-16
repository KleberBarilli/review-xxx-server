package com.idealizer.review_x.application.modules.actors.controllers.mappers;

import com.idealizer.review_x.application.modules.actors.controllers.dto.ActorDTO;
import com.idealizer.review_x.application.modules.actors.entities.Actor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ActorMapper {
    @Mapping(target = "person.name", source = "name")
    @Mapping(target = "person.birthDate", source = "birthDate")
    @Mapping(target = "person.avatarUrl", source = "avatarUrl")
    @Mapping(target = "person.description", source = "description")
    @Mapping(target = "person.nationality", source = "nationality")
    Actor toEntity(ActorDTO dto);

    @Mapping(target = "name", source = "person.name")
    @Mapping(target = "birthDate", source = "person.birthDate")
    @Mapping(target = "avatarUrl", source = "person.avatarUrl")
    @Mapping(target = "description", source = "person.description")
    @Mapping(target = "nationality", source = "person.nationality")
    @Mapping(target = "createdAt", source = "person.createdAt")
    @Mapping(target = "updatedAt", source = "person.updatedAt")

    ActorDTO toDTO(Actor actor);
}
