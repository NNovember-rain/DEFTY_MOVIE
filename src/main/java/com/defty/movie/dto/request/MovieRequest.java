package com.defty.movie.dto.request;


import lombok.Data;
import lombok.experimental.FieldDefaults;
import lombok.AccessLevel;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MovieRequest {

    String title;

    String description;

    MultipartFile trailer;

    MultipartFile thumbnail;

    MultipartFile coverImage;

//    Integer status;

    String nation;

    Date releaseDate;

    Integer membershipType;

    Integer ranking;

//    String slug;

    String director;
}

