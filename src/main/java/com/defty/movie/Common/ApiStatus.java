package com.defty.movie.Common;


import lombok.Getter;

@Getter

public enum ApiStatus {
    SUCCESS(200, "Success"),
    UNAUTHORIZED(401, "Unauthorized: Invalid credentials"),
    INTERNAL_ERROR(500, "Internal Server Error"),
    BAD_REQUEST(400, "Bad request");

    private final int code;
    private final String message;

    ApiStatus(int code, String message) {
        this.code = code;
        this.message = message;
    }

}
