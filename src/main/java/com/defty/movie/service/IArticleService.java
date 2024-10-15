package com.defty.movie.service;

import com.defty.movie.dto.request.ArticleRequest;
import com.defty.movie.dto.response.ArticlePageableResponse;
import com.defty.movie.dto.response.ArticleResponse;
import com.defty.movie.entity.Article;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IArticleService {
    Integer addArticle(ArticleRequest articleRequest);
    void updateArticle(Integer id, ArticleRequest articleRequest);
    void deleteArticle(List<Integer> ids);
    ArticleResponse getArticle(Integer id);
    ArticlePageableResponse getAllArticles(Pageable pageable);
    Long getArticleCount();
}
