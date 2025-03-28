package com.defty.movie.dto.response;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.Date;
import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MovieSearchResultResponse {
    Integer id;

    String title;

    String description;

    String thumbnail;

    Integer status;

    Date releaseDate;

    Integer membershipType;

    String slug;

    DirectorNameResponse director;

    List<ActorNameResponse> actors;

    List<EpisodeNameResponse> episodes;
}
