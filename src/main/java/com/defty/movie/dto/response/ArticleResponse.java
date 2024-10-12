package com.defty.movie.dto.response;

import jakarta.persistence.Column;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ArticleResponse {

    Integer id;

    String title;

    String content;

    String author;

    String thumbnail;

    String slug;


}
