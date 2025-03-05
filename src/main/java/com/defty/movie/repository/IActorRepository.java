package com.defty.movie.repository;

import com.defty.movie.entity.Actor;
import com.defty.movie.entity.Director;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

public interface IActorRepository extends JpaRepository<Actor, Integer> {
    @Query(value = "SELECT m FROM Actor m WHERE " +
            "(:name IS NULL OR m.fullName LIKE %:name%) AND " +
            "(:gender IS NULL OR m.gender LIKE %:gender%) AND " +
            "((:startDate IS NULL AND :endDate IS NULL) OR " +
            "(DATE(m.dateOfBirth) BETWEEN DATE(:startDate) AND DATE(:endDate))) AND " +
            "(:nationality IS NULL OR m.nationality LIKE %:nationality%) AND " +
            "(m.status != -1) AND " +
            "(:status IS NULL OR m.status = :status) " +
            "ORDER BY m.createdDate DESC",
            countQuery = "SELECT count(m) FROM Actor m WHERE " +
                    "(:name IS NULL OR m.fullName LIKE %:name%) AND " +
                    "(:gender IS NULL OR m.gender LIKE %:gender%) AND " +
                    "((:startDate IS NULL AND :endDate IS NULL) OR " +
                    "(DATE(m.dateOfBirth) BETWEEN DATE(:startDate) AND DATE(:endDate))) AND " +
                    "(:nationality IS NULL OR m.nationality LIKE %:nationality%) AND " +
                    "(m.status != -1) AND " +
                    "(:status IS NULL OR m.status = :status)",
            nativeQuery = false)
    Page<Actor> findActors(
            @Param("name") String name,
            @Param("gender") String gender,
            @Param("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @Param("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
            @Param("nationality") String nationality,
            @Param("status") Integer status,
            Pageable pageable);

    @Query(value = "SELECT a.* FROM actor a " +
            "JOIN actor_movie am ON a.id = am.actor_id " +
            "JOIN movie m ON am.movie_id = m.id " +
            "WHERE am.movie_id = :movieId " +
            "ORDER BY m.release_date DESC " +  // Sắp xếp theo release_date của movie
            "LIMIT 2", nativeQuery = true)
    List<Actor> findTop2NewestActorsByMovieId(@Param("movieId") Integer movieId);


}
