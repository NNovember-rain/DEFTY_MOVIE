package com.defty.movie.dto.request;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EpisodeRequest {
    Integer id;
    Integer number;
    String description;
    String thumbnail;
    String link;
    Integer movieId;
}
