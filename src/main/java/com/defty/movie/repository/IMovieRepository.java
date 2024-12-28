package com.defty.movie.repository;

import com.defty.movie.entity.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public interface IMovieRepository extends JpaRepository<Movie, Integer>, JpaSpecificationExecutor<Movie> {
    Page<Movie> findAll(Specification<Movie> spec, Pageable pageable);
    @Query(value = "SELECT m FROM Movie m WHERE " +
            "(:title IS NULL OR m.title LIKE %:title%) AND " +
            "(:nation IS NULL OR m.nation LIKE %:nation%) AND " +
            "(:releaseDate IS NULL OR DATE(m.releaseDate) = :releaseDate) AND " +
            "(:ranking IS NULL OR m.ranking = :ranking) AND " +
            "(:directorId IS NULL OR m.director.id = :directorId) AND " +
            "(m.status != -1) AND " +
            "(:status IS NULL OR m.status = :status) " +
            "ORDER BY m.createdDate DESC",
            countQuery = "SELECT count(m) FROM Movie m WHERE " +
                    "(:title IS NULL OR m.title LIKE %:title%) AND " +
                    "(:nation IS NULL OR m.nation LIKE %:nation%) AND " +
                    "(:releaseDate IS NULL OR DATE(m.releaseDate) = :releaseDate) AND " +
                    "(:ranking IS NULL OR m.ranking = :ranking) AND " +
                    "(:directorId IS NULL OR m.director.id = :directorId) AND " +
                    "(m.status != -1) AND " +
                    "(:status IS NULL OR m.status = :status)",
            nativeQuery = false)
    Page<Movie> findMovies(
            @Param("title") String title,
            @Param("nation") String nation,
            @Param("releaseDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date releaseDate,
            @Param("ranking") Integer ranking,
            @Param("directorId") Integer directorId,
            @Param("status") Integer status,
            Pageable pageable);
}
