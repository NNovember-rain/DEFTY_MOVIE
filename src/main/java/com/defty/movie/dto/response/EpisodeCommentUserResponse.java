package com.defty.movie.dto.response;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EpisodeCommentUserResponse {
    Integer id;
    String fullName;
    String avatar;
    String slug;
}
