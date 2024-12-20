package com.defty.movie.utils;

import com.defty.movie.dto.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ApiResponeUtil {
    public static <T> ResponseEntity<T> ResponseOK(T data) {
        ApiResponse<?> apiResponse = ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .data(data)
                .build();
        return (ResponseEntity<T>) ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    public static <T> ResponseEntity<T> ResponseCreatedSuccess(T data) {
        ApiResponse<?> apiResponse = ApiResponse.builder()
                .status(HttpStatus.CREATED.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .data(data)
                .build();
        return (ResponseEntity<T>) ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }
}
