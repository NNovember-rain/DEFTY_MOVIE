package com.defty.movie.dto.response;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.Date;
import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MovieDetailResponse {
    Integer id;
    String title;
    String rating;
    Date releaseDate;
    Integer duration;
    String description;
    String coverImage;
    String trailer;
    String slug;
    MovieNameResponse director;
    List<CategoryNameResponse> category;
    List<ActorNameResponse> actor;
}
