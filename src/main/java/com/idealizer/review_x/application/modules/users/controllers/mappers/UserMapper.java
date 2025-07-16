package com.idealizer.review_x.application.modules.users.controllers.mappers;

import com.idealizer.review_x.application.modules.users.controllers.dto.SocialLoginDTO;
import org.mapstruct.Mapper;

import com.idealizer.review_x.application.modules.users.entities.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toEntity(SocialLoginDTO dto);

    SocialLoginDTO toDTO(User user);
}
