package com.defty.movie.dto.request;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MovieCommentRequest {
    Integer episodeId;
    Integer parentEpisodeCommentId;
    String content;
}
