package com.defty.movie.service;

import com.defty.movie.dto.request.ArticleRequest;
import com.defty.movie.entity.Article;

public interface IArticleService {
    void addArticle(ArticleRequest articleRequest);
    void updateArticle(Integer id, ArticleRequest articleRequest);
    void deleteArticle(Integer id);
}
