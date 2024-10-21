package com.defty.movie.dto.response;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ActorResponse {
    String fullName;

    String gender;

    String dateOfBirth;

    Integer weight;

    Integer height;

    String nationality;

    String description;

    String avatar;
}
