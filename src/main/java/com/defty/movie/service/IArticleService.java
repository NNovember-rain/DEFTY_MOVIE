package com.defty.movie.service;

import com.defty.movie.dto.request.ArticleRequest;
import com.defty.movie.entity.Article;

import java.util.List;

public interface IArticleService {
    void addArticle(ArticleRequest articleRequest);
    void updateArticle(Integer id, ArticleRequest articleRequest);
    void deleteArticle(List<Integer> ids);
}
