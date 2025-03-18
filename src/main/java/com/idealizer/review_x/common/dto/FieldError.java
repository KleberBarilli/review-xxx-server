package com.idealizer.review_x.common.dto;

public record FieldError(String field, String message) {

    @Override
    public String field() {
        return field;
    }

    @Override
    public String message() {
        return message;
    }
}
