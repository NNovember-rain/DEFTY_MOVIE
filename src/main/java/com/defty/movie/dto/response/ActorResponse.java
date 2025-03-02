package com.defty.movie.dto.response;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.Date;
import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ActorResponse {
    Integer id;

    String fullName;

    String gender;

    Date dateOfBirth;

    Integer weight;

    Integer height;

    String nationality;

    Integer orderIndex;

    String description;

    String avatar;

    Integer status;

    List<MovieResponse> movies;
}
