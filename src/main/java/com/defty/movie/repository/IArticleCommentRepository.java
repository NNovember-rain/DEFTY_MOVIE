package com.defty.movie.repository;

import com.defty.movie.entity.ArticleComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface IArticleCommentRepository extends JpaRepository<ArticleComment, Integer> {
    Optional<List<ArticleComment>> findByArticleIdAndStatus(Integer articleId, int status);
    Optional<List<ArticleComment>> findByParentArticleComment_Id(Integer articleId);
}
