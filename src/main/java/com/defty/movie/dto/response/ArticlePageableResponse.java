package com.defty.movie.dto.response;

import com.defty.movie.entity.Article;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class ArticlePageableResponse {

    private List<ArticleResponse> articleResponses;
    private Long totalElements;

}
