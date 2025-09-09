package com.idealizer.review_x.application.user.validators;

import com.idealizer.review_x.common.MessageUtil;
import com.idealizer.review_x.domain.core.user.entities.User;
import org.springframework.stereotype.Component;

@Component
public class UserValidator {
    private MessageUtil messageUtil;

    public UserValidator(MessageUtil messageUtil) {
        this.messageUtil = messageUtil;
    }

    public void validate(User user) {
        // if (user.getBio().length() > 10) {
        // throw new IllegalArgumentException(messageUtil.get("user.bio.tooLong",
        // null,LocaleUtil.from(user.getLocale())));
        // }
    }

}
