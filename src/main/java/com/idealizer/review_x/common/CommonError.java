package com.idealizer.review_x.common;

import org.springframework.http.HttpStatus;

public enum CommonError {

    INTERNAL_ERROR("E-0000", HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected internal error"),

    EMAIL_OR_USERNAME_ALREADY_EXISTS("E-1000", HttpStatus.CONFLICT, "Username or Email already exists"),
    EMAIL_ALREADY_EXISTS("E-1010", HttpStatus.CONFLICT, "Email already exists"),
    USERNAME_ALREADY_EXISTS("E-1020", HttpStatus.CONFLICT, "Username already exists"),
    CREATE_USER_GENERIC_ERROR("E-1021", HttpStatus.BAD_REQUEST, "Create user generic error"),
    UPDATE_USER_GENERIC_ERROR("E-1022", HttpStatus.BAD_REQUEST, "Update user generic error"),
    INVALID_CREDENTIALS("E-1030", HttpStatus.UNAUTHORIZED, "Invalid credentials");

    private final String code;
    private final HttpStatus status;
    private final String defaultMessage;

    CommonError(String code, HttpStatus status, String defaultMessage) {
        this.code = code;
        this.status = status;
        this.defaultMessage = defaultMessage;
    }

    public String code() {
        return code;
    }

    public HttpStatus status() {
        return status;
    }

    public String defaultMessage() {
        return defaultMessage;
    }

    public static CommonError fromCode(String code) {
        if (code == null)
            return null;
        for (var e : values())
            if (e.code.equalsIgnoreCase(code))
                return e;
        return null;
    }
}
