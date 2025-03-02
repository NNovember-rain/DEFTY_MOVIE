package com.defty.movie.dto.response;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.Date;
import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ActorMovieDetailResponse {
    Integer id;

    String fullName;

    String gender;

    Date dateOfBirth;

    Integer weight;

    Integer height;

    String nationality;

    Integer position;

    String description;

    String avatar;

    Integer status;

    List<MovieResponse> movies;
}
