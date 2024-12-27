package com.defty.movie.dto.response;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MovieResponse {
    Integer id;

    String title;

    String description;

    String trailer;

    MultipartFile thumbnail;

    String coverImage;

//    Integer status;

    String nation;

    Date releaseDate;

    Integer membershipType;

    Integer ranking;

    String slug;

    String director;

    Date createdDate;

    String createdBy;

    Date modifiedDate;

    String modifiedBy;
}
