package com.defty.movie.dto.response;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryResponse {
    Integer id;
    String name;
    String description;
    Integer numberOfMovies;
    Integer status;
    String slug;
}
