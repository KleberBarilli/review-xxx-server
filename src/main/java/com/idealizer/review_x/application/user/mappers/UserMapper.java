package com.idealizer.review_x.application.user.mappers;

import com.idealizer.review_x.application.user.responses.FindUserResponse;
import com.idealizer.review_x.common.dtos.user.UpdateUserDTO;
import com.idealizer.review_x.infra.http.controllers.user.dto.SignupRequestDTO;
import org.bson.types.ObjectId;
import org.mapstruct.*;

import com.idealizer.review_x.domain.core.user.entities.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toEntity(SignupRequestDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateUserFromDto(UpdateUserDTO dto, @MappingTarget User user);


    @Mapping(target = "username", source = "name")
    FindUserResponse toDetailedUser (User user);

    default String map(ObjectId value) {
        return value != null ? value.toHexString() : null;
    }
}
