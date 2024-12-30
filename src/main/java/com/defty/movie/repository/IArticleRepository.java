package com.defty.movie.repository;

import com.defty.movie.entity.Article;
import com.defty.movie.repository.custom.IArticleRepositoryCustom;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface IArticleRepository extends JpaRepository<Article, Integer>, IArticleRepositoryCustom {
    Optional<Article> findById(Integer id);
    Article findBySlug(String slug);
    Page<Article> findAllByStatus(Integer status,Pageable pageable);
    Long countByStatus(Integer status);
    Page<Article> findByTitleContaining(String title, Pageable pageable);
}
