package com.defty.movie.service;

import com.defty.movie.dto.request.ArticleRequest;
import com.defty.movie.dto.response.ArticleResponse;
import com.defty.movie.dto.response.PageableResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

public interface IArticleService {
    Integer addArticle(ArticleRequest articleRequest);
    void updateArticle(Integer id, ArticleRequest articleRequest);
    void deleteArticle(List<Integer> ids);
    ArticleResponse getArticle(Integer id);
    PageableResponse<ArticleResponse> getAllArticles(Pageable pageable);
    Long getArticleCount();
    PageableResponse<ArticleResponse> findArticles(Pageable pageable, Map<String, Object> params);
}
