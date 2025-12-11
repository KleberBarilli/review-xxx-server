package com.idealizer.review_x.application.user.usecases;

import com.idealizer.review_x.common.CommonError;
import com.idealizer.review_x.common.exceptions.BadRequestException;
import com.idealizer.review_x.common.exceptions.DuplicatedException;
import com.idealizer.review_x.common.exceptions.NotFoundException; // Assumindo que você tem essa exception
import com.idealizer.review_x.domain.core.user.repositories.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
public class UpdateUsernameUseCase {

    private final UserRepository userRepository;

    private static final Pattern USERNAME_PATTERN = Pattern.compile("^[a-zA-Z0-9_.]{3,20}$");

    public UpdateUsernameUseCase(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public void execute(ObjectId userId, String username) {
        if (username == null || !USERNAME_PATTERN.matcher(username).matches()) {
            throw new BadRequestException(CommonError.UPDATE_USERNAME_REGEX_ERROR.code());
        }

        String cleanUsername = username.trim();

        try {
            long modifiedCount = userRepository.updateNameById(userId, cleanUsername);

            if (modifiedCount == 0) {
                if (!userRepository.existsById(userId)) {
                    throw new NotFoundException("User not found");
                }
            }
        } catch (DuplicateKeyException ex) {
            throw new DuplicatedException(CommonError.EMAIL_OR_USERNAME_ALREADY_EXISTS.code());
        }
    }
}