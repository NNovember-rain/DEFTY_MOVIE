package com.defty.movie.repository;

import com.defty.movie.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ICategoryRepository extends JpaRepository<CategoryEntity, Integer> {
}
