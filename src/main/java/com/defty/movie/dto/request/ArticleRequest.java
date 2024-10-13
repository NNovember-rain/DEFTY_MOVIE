package com.defty.movie.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ArticleRequest {

    @NotNull(message = "required")
    Integer id;

    @NotNull(message = "required")
    String title;

    String content;

    String author;

    String thumbnail;

}

