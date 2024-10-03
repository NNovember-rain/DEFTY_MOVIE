package com.defty.movie.repository;

import com.defty.movie.entity.Account;
import com.defty.movie.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IAriticleRepository extends JpaRepository<Article, Integer> {
}
