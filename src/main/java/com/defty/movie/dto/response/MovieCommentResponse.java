package com.defty.movie.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class MovieCommentResponse {
    Integer id;
    String content;
    Date createdAt;
    String replyTo; // Trường này sẽ chỉ có giá trị khi lấy replies bằng API thứ 21
    Integer parenCommentId;
    EpisodeCommentUserResponse user;
    List<CommentReactionResponse> reactions;
    Integer toTalReply;
}