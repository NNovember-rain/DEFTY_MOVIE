package com.defty.movie.dto.response;

import java.util.List;

public class DetailMovieResponse {
    String name;
    String rating;
    String year;
    String duration;
    List<String> category;
    List<String> director;
    List<String> actor;
    String description;
    List<EpisodeResponse> episodes;
}
