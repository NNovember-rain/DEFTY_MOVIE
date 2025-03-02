package com.defty.movie.repository;

import com.defty.movie.entity.Actor;
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
import java.util.List;
import java.util.Optional;

public interface IMovieRepository extends JpaRepository<Movie, Integer>, JpaSpecificationExecutor<Movie> {
    Page<Movie> findAll(Specification<Movie> spec, Pageable pageable);
    @Query(value = "SELECT m FROM Movie m WHERE " +
            "(:title IS NULL OR m.title LIKE %:title%) AND " +
            "(:nation IS NULL OR m.nation LIKE %:nation%) AND " +
            "((:startReleaseDate IS NULL AND :endReleaseDate IS NULL) OR " +
            "(DATE(m.releaseDate) BETWEEN DATE(:startReleaseDate) AND DATE(:endReleaseDate))) AND " +
            "(:ranking IS NULL OR m.ranking = :ranking) AND " +
            "(:directorId IS NULL OR m.director.id = :directorId) AND " +
            "(m.status != -1) AND " +
            "(:status IS NULL OR m.status = :status) " +
            "ORDER BY m.createdDate DESC",
            countQuery = "SELECT count(m) FROM Movie m WHERE " +
                    "(:title IS NULL OR m.title LIKE %:title%) AND " +
                    "(:nation IS NULL OR m.nation LIKE %:nation%) AND " +
                    "((:startReleaseDate IS NULL AND :endReleaseDate IS NULL) OR " +
                    "(DATE(m.releaseDate) BETWEEN DATE(:startReleaseDate) AND DATE(:endReleaseDate))) AND " +
                    "(:ranking IS NULL OR m.ranking = :ranking) AND " +
                    "(:directorId IS NULL OR m.director.id = :directorId) AND " +
                    "(m.status != -1) AND " +
                    "(:status IS NULL OR m.status = :status)",
            nativeQuery = false)
    Page<Movie> findMovies(
            @Param("title") String title,
            @Param("nation") String nation,
            @Param("startReleaseDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startReleaseDate,
            @Param("endReleaseDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endReleaseDate,
            @Param("ranking") Integer ranking,
            @Param("directorId") Integer directorId,
            @Param("status") Integer status,
            Pageable pageable);


    @Query(value = "SELECT m.* FROM movie m " +
            "JOIN actor_movie am ON m.id = am.movie_id " +
            "JOIN actor a ON am.actor_id = a.id " +
            "WHERE am.actor_id = :actorId and m.status=1 " +
            "ORDER BY m.release_date DESC " +  // Sắp xếp theo release_date của movie
            "LIMIT 2", nativeQuery = true)
    List<Movie> findTop2NewestMoviesByActorId(@Param("actorId") Integer actorId);


    @Query(value = "SELECT m.* FROM movie m " +
            "JOIN director d ON d.id = m.director_id " +
            "WHERE m.director_id = :directorId and m.status=1 " +
            "ORDER BY m.release_date DESC " +  // Sắp xếp theo release_date của movie
            "LIMIT 2", nativeQuery = true)
    List<Movie> findTop2NewestMoviesByDirectorId(@Param("directorId") Integer directorId);


    @Query("SELECT m FROM Movie m WHERE NOT EXISTS (" +
            "SELECT b FROM Banner b WHERE b.contentType = 'Movie' AND b.contentId = m.id) " +
            "AND (:title IS NULL OR m.title LIKE %:title%)"+"AND m.status=1")
    List<Movie> findAllMoviesNotInBanner(@Param("title") String title);

    Optional<Movie> findBySlugAndStatus(String slug, Integer status);
}
