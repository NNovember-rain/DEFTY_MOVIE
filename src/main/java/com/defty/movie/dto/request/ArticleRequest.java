package com.defty.movie.dto.request;

import jakarta.persistence.Column;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ArticleRequest {

    Integer id;

    String content;

    String author;

    String thumbnail;

}
