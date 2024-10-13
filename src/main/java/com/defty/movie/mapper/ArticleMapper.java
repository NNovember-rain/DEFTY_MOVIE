package com.defty.movie.mapper;

import com.defty.movie.dto.request.ArticleRequest;
import com.defty.movie.dto.response.ArticleResponse;
import com.defty.movie.entity.Article;
import com.defty.movie.service.impl.AccountService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class ArticleMapper {
    private final ModelMapper modelMapper;
    AccountService accountService;

    public Article toArticleEntity(ArticleRequest articleRequest) {
        Article article = modelMapper.map(articleRequest, Article.class);
        return article;
    }

    public ArticleResponse toArticleResponse(Article article) {
        ArticleResponse articleResponse = modelMapper.map(article, ArticleResponse.class);
        return articleResponse;
    }
}
