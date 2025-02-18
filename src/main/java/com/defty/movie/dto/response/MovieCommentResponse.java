package com.defty.movie.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

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
    List<CommentReactionResponse> commentReactionRespons;
}
