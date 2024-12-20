package com.defty.movie.service;

import com.defty.movie.dto.request.ArticleCommentRequest;
import com.defty.movie.entity.ArticleComment;

public interface IArticleCommentService {
    Integer addArticleComment(ArticleCommentRequest articleCommentRequest);
}
