package com.defty.movie.dto.response;

import com.defty.movie.entity.Category;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class MovieAppSearchResultResponse {
    Integer id;

    String title;

    String description;

    String thumbnail;

    String rating;

    Date releaseDate;

    Integer membershipType;

    String slug;

    List<String> categories;

    List<String> actors;

}
