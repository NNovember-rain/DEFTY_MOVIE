package com.defty.movie.repository;

import com.defty.movie.entity.Article;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IAriticleRepository extends JpaRepository<Article, Integer> {

    Article findBySlug(String slug);
    Page<Article> findAll(Pageable pageable);
}
