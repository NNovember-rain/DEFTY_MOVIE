package com.defty.movie.converter;

import com.defty.movie.dto.request.ArticleRequest;
import com.defty.movie.entity.Article;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@RequiredArgsConstructor
public class ArticleConverter {
    private final ModelMapper modelMapper;

    public Article ArticleRequestToArticleEntity(ArticleRequest articleRequest) {
        Article article = modelMapper.map(articleRequest, Article.class);
        return article;
    }
}
