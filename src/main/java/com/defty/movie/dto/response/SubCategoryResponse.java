package com.defty.movie.dto.response;

import lombok.Data;

import java.util.Date;
@Data
public class SubCategoryResponse {
    String movieThumbnail;
    String movieTitle;
    String description;
    Integer numberOfChild;
    Date releaseDate;
}
