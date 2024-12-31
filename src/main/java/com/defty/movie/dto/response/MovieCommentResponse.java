package com.defty.movie.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MovieCommentResponse {
    int id;
    int episodeId;
    int episodeCommentParentId;
    String content;
}
