package com.idealizer.review_x.modules.users.validators;

import com.idealizer.review_x.common.LocaleUtil;
import com.idealizer.review_x.common.MessageUtil;
import com.idealizer.review_x.modules.users.entities.User;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class UserValidator {
    private MessageUtil messageUtil;


    public UserValidator(MessageUtil messageUtil) {
        this.messageUtil = messageUtil;
    }


    public void validate(User user) {
//        if (user.getBio().length() > 10) {
//            throw new IllegalArgumentException(messageUtil.get("user.bio.tooLong", null,LocaleUtil.from(user.getLocale())));
//        }
    }


}
