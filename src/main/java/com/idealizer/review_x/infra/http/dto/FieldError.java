package com.idealizer.review_x.infra.http.dto;

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
