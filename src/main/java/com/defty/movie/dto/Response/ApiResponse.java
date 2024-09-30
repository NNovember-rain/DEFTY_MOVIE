package com.defty.movie.dto.Response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)

public class ApiResponse<T> {
    int status;
    String message;
    T data;
}
