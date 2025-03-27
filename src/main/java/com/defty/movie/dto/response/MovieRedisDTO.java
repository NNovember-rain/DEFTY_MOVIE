package com.defty.movie.dto.response;

import lombok.Data;

@Data
public class MovieRedisDTO {
    private String name;
    private String nameNoAccent;
    private String slug;
}
