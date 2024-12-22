package com.defty.movie.dto.response;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EpisodeResponse {
    Integer id;
    Integer number;
    String description;
    String thumbnail;
    String link;
    Integer movieId;
}
