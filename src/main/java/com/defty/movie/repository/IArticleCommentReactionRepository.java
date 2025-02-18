package com.defty.movie.repository;

import com.defty.movie.entity.ArticleCommentReaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IArticleCommentReactionRepository extends JpaRepository<ArticleCommentReaction, Integer> {
    List<ArticleCommentReaction> findByArticleCommentId(Integer articleId);
}
