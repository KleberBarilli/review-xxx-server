package com.idealizer.review_x.common;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class MessageUtil {

    private final MessageSource messageSource;

    public MessageUtil(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public String get(String key, Locale locale) {
        return messageSource.getMessage(key, null, locale);
    }

    public String get(String key, Object[] args, Locale locale) {
        return messageSource.getMessage(key, args, locale);
    }

    public String get(String key) {
        return messageSource.getMessage(key, null, LocaleContextHolder.getLocale());
    }
}
