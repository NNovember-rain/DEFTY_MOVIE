package com.defty.movie.repository;

import com.defty.movie.entity.Article;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IArticleRepository extends JpaRepository<Article, Integer> {
    Optional<Article> findById(Integer id);
    Article findBySlug(String slug);
    Page<Article> findAllByStatus(Integer status,Pageable pageable);
    List<Article> findAllByStatus(Integer status);
    Long countByStatus(Integer status);
    Page<Article> findByStatusAndTitleContains(Integer status,String title, Pageable pageable);
    List<Article> findByStatusAndTitleContains(Integer status, String title);
}
