package com.defty.movie.dto.response;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MovieResponse {
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

    String slug;

    Integer directorId;

    Date createdDate;

    String createdBy;

    Date modifiedDate;

    String modifiedBy;
}
