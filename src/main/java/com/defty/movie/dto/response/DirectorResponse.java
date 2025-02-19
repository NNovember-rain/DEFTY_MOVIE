package com.defty.movie.dto.response;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DirectorResponse {
    Integer id;

    String fullName;

    String gender;

    String dateOfBirth;

    Integer weight;

    Integer height;

    Integer order;

    String nationality;

    String description;

    String avatar;

    Integer status;
}
