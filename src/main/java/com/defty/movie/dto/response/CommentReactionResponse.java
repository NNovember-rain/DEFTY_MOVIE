package com.defty.movie.dto.response;


import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentReactionResponse {

    String content;

    UserResponse userResponse;

}
