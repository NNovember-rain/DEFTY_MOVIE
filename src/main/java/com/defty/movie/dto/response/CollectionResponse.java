package com.defty.movie.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class CollectionResponse {
    private Integer id;
    private String name;
    private String description;
    private Integer status;
    private String slug;
    private List<CategoryResponse> categories;
    private List<MovieResponse> movies;
    private List<DirectorResponse> directors;
    private List<ActorResponse> actors;
}
