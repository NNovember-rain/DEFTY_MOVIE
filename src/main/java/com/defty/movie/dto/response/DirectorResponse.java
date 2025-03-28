package com.defty.movie.dto.response;

import com.defty.movie.entity.Director;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.Date;

import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DirectorResponse {
    Integer id;

    String fullName;

    String gender;

    Date dateOfBirth;

    Integer weight;

    Integer height;

    Integer orderIndex;

    String nationality;

    String description;

    String avatar;

    Integer status;

    String slug;

    public static DirectorResponse fromEntity(Director director) {
        DirectorResponse response = new DirectorResponse();
        response.setId(director.getId());
        response.setFullName(director.getFullName());
        return response;
    }
}
