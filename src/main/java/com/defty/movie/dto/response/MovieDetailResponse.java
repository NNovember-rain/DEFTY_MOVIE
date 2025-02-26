package com.defty.movie.dto.response;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.Date;
import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MovieDetailResponse {
    String title;
    String rating;
    Date releaseDate;
    Integer duration;
    String description;
    String image;
    String trailer;
    String director;
    List<String> category;
    List<String> actor;
}
