package com.defty.movie.dto.request;


import lombok.Data;
import lombok.experimental.FieldDefaults;
import lombok.AccessLevel;
import jakarta.validation.constraints.NotNull;
import java.util.Date;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MovieRequest {
    Integer id;

    String title;

    String description;

    String trailer;

    String thumbnail;

    String coverImage;

//    Integer status;

    String nation;

    Date releaseDate;

    Boolean membershipType;

    Integer ranking;

//    String slug;

    Integer directorId;
}

