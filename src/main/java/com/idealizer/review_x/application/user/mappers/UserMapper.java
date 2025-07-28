package com.idealizer.review_x.application.user.mappers;

import com.idealizer.review_x.application.user.responses.CurrentLoggedUserResponse;
import com.idealizer.review_x.infra.http.modules.user.dto.SignupRequestDTO;
import org.bson.types.ObjectId;
import org.mapstruct.Mapper;

import com.idealizer.review_x.domain.user.entities.User;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toEntity(SignupRequestDTO dto);

    SignupRequestDTO toDTO(User user);

    @Mapping(target = "username", source = "name")
    CurrentLoggedUserResponse toCurrentLoggedUser (User user);

    default String map(ObjectId value) {
        return value != null ? value.toHexString() : null;
    }
}
