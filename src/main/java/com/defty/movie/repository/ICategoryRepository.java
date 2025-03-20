package com.defty.movie.repository;

import com.defty.movie.entity.Actor;
import com.defty.movie.entity.Category;
import com.defty.movie.entity.Movie;
import com.defty.movie.entity.MovieCategory;
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

    @Query("SELECT c FROM Category c WHERE NOT EXISTS (" +
            "SELECT b FROM Banner b WHERE b.contentType = 'Category' AND b.contentId = c.id) " +
            "AND (:title IS NULL OR c.name LIKE %:title%)"+"AND c.status=1")
    List<Category> findAllCategoriesNotInBanner(@Param("title") String title);

    @Query(value = """
        SELECT mc FROM MovieCategory mc 
        WHERE (:isInCategory = TRUE AND mc.category.id = :categoryId 
               OR :isInCategory = FALSE AND mc.category.id != :categoryId) 
        AND (:title IS NULL OR mc.movie.title LIKE %:title%) 
        AND (:nation IS NULL OR mc.movie.nation LIKE %:nation%) 
        AND (
            :startReleaseDate IS NULL AND :endReleaseDate IS NULL 
            OR mc.movie.releaseDate BETWEEN :startReleaseDate AND :endReleaseDate
        ) 
        AND (:ranking IS NULL OR mc.movie.ranking = :ranking) 
        AND (:directorId IS NULL OR mc.movie.director.id = :directorId) 
        AND mc.movie.status != -1 
        ORDER BY mc.movie.createdDate DESC
    """,
            countQuery = """
        SELECT count(mc) FROM MovieCategory mc 
        WHERE (:isInCategory = TRUE AND mc.category.id = :categoryId 
               OR :isInCategory = FALSE AND mc.category.id != :categoryId) 
        AND (:title IS NULL OR mc.movie.title LIKE %:title%) 
        AND (:nation IS NULL OR mc.movie.nation LIKE %:nation%) 
        AND (
            :startReleaseDate IS NULL AND :endReleaseDate IS NULL 
            OR mc.movie.releaseDate BETWEEN :startReleaseDate AND :endReleaseDate
        ) 
        AND (:ranking IS NULL OR mc.movie.ranking = :ranking) 
        AND (:directorId IS NULL OR mc.movie.director.id = :directorId) 
        AND mc.movie.status != -1
    """)
    Page<MovieCategory> findMoviesByCategory(
            @Param("categoryId") Integer categoryId,
            @Param("isInCategory") boolean isInCategory,
            @Param("title") String title,
            @Param("nation") String nation,
            @Param("startReleaseDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startReleaseDate,
            @Param("endReleaseDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endReleaseDate,
            @Param("ranking") Integer ranking,
            @Param("directorId") Integer directorId,
            Pageable pageable);


}
