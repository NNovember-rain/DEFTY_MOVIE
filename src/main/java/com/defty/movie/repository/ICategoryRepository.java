package com.defty.movie.repository;

import com.defty.movie.entity.Actor;
import com.defty.movie.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

public interface ICategoryRepository extends JpaRepository<Category, Integer> {
    @Query(value = "SELECT m FROM Category m WHERE " +
            "(:name IS NULL OR m.name LIKE %:name%) AND" +
            "(m.status != -1) AND " +
            "(:status IS NULL OR m.status = :status) " +
            "ORDER BY m.createdDate DESC",
            countQuery = "SELECT count(m) FROM Category m WHERE " +
                    "(:name IS NULL OR m.name LIKE %:name%) AND " +
                    "(m.status != -1) AND " +
                    "(:status IS NULL OR m.status = :status)",
            nativeQuery = false)
    Page<Category> findCategories(
            @Param("name") String name,
            @Param("status") Integer status,
            Pageable pageable);

    @Query(value="SELECT c FROM Category c WHERE NOT EXISTS (" +
            "SELECT b FROM Banner b WHERE b.contentType = 'Category' AND b.contentId = c.id)"
            ,nativeQuery = false)
    List<Category> findAllCategoriesNotInBanner();
}
