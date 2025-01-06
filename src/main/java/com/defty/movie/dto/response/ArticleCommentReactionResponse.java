package com.defty.movie.dto.response;


import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ArticleCommentReactionResponse {

    String content;

    UserResponse userResponse;

}
