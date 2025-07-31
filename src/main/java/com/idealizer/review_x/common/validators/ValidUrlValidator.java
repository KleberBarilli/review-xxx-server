package com.idealizer.review_x.common.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class ValidUrlValidator implements ConstraintValidator<ValidUrl, List<String>> {
    @Override
    public boolean isValid(List<String> urls, ConstraintValidatorContext context) {
        if (urls == null) return true;
        for (String url : urls) {
            try {
                new URL(url);
            } catch (MalformedURLException e) {
                return false;
            }
        }
        return true;
    }
}