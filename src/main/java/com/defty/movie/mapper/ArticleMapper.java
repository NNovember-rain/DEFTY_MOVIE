package com.defty.movie.mapper;

import com.defty.movie.dto.request.ArticleRequest;
import com.defty.movie.entity.Article;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ArticleMapper {
    private final ModelMapper modelMapper;

    public Article toArticleEntity(ArticleRequest articleRequest) {
        Article article = modelMapper.map(articleRequest, Article.class);
        return article;
    }
}
