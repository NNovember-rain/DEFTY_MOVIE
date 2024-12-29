package com.defty.movie.service;

import com.defty.movie.dto.request.ArticleCommentRequest;
import com.defty.movie.dto.response.ArticleCommentResponse;
import com.defty.movie.entity.ArticleComment;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface IArticleCommentService {
    Integer addArticleComment(ArticleCommentRequest articleCommentRequest);
    void updateArticleComment(Integer id, ArticleCommentRequest articleCommentRequest);
    void deleteArticleComment(List<Integer> ids);
    List<ArticleCommentResponse> getArticleComment(Integer articleId);
}
