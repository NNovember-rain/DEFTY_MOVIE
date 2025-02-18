package com.defty.movie.service;

import com.defty.movie.dto.request.ArticleCommentReactionRequest;
import com.defty.movie.dto.request.ArticleCommentReactionUpdateRequest;

public interface IArticleCommentReactionService {
    Integer addArticleCommentReaction(ArticleCommentReactionRequest articleCommentReactionRequest);
    void updateArticleCommentReaction(ArticleCommentReactionUpdateRequest articleCommentReactionUpdateRequest);
    void deleteArticleCommentReaction(Integer articleCommentReactionId);
}
