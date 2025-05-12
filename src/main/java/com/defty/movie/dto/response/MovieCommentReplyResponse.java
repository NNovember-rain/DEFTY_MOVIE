package com.defty.movie.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class MovieCommentReplyResponse {
    int id;
    String content;
    Date createdAt;
    EpisodeCommentUserResponse user;
    List<CommentReactionResponse> reactions;
}
