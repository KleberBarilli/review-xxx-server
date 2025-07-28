package com.idealizer.review_x.application.user.mappers;

import com.idealizer.review_x.infra.http.modules.user.dto.SignupRequestDTO;
import org.mapstruct.Mapper;

import com.idealizer.review_x.domain.user.entities.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toEntity(SignupRequestDTO dto);

    SignupRequestDTO toDTO(User user);
}
