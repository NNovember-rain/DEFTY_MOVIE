package com.defty.movie.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ArticleCommentResponse {
    int id;
    int articleId;
    int articleCommentParentId;
    String content;
    List<CommentReactionResponse> commentReactionRespons;
}
