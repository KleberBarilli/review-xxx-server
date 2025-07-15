package com.idealizer.review_x.common;

import java.util.Locale;

public class LocaleUtil {

    public static Locale from(String locale) {
        if (locale == null || locale.isBlank()) {
            return Locale.ENGLISH;
        }
        return Locale.forLanguageTag(locale.replace('_', '-'));
    }

}
