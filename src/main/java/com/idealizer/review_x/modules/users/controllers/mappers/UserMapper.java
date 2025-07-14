package com.idealizer.review_x.modules.users.controllers.mappers;

import org.mapstruct.Mapper;

import com.idealizer.review_x.modules.users.controllers.dto.UserDTO;
import com.idealizer.review_x.modules.users.entities.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toEntity(UserDTO dto);

    UserDTO toDTO(User user);
}
