package com.idealizer.review_x.modules.users.controllers.mappers;

import com.idealizer.review_x.modules.users.controllers.dto.UserDTO;
import com.idealizer.review_x.modules.users.entities.User;
import org.mapstruct.Mapping;

public interface UserMapper {
    User toEntity(UserDTO dto);
    UserDTO toDTO(User user);
}
