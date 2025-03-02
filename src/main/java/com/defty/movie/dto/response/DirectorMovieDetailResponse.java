package com.defty.movie.dto.response;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DirectorMovieDetailResponse {
    Integer id;

    String fullName;

    String gender;

    String dateOfBirth;

    Integer weight;

    Integer height;

    Integer position;

    String nationality;

    String description;

    String avatar;

    Integer status;

    List<MovieResponse> movies;
}
