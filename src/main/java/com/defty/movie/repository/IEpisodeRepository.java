package com.defty.movie.repository;

import com.defty.movie.entity.Episode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface IEpisodeRepository extends JpaRepository<Episode, Integer> {
    @Query(value = "SELECT e FROM Episode e WHERE " +
            "(:number IS NULL OR e.number = :number) AND " +
            "(:status IS NULL OR e.status = :status) AND " +
            "(e.status != -1) AND " +
            "(:movieId IS NULL OR e.movie.id = :movieId) " +
            "ORDER BY e.number ASC, e.createdDate DESC",
            countQuery = "SELECT count(e) FROM Episode e WHERE " +
                    "(:number IS NULL OR e.number = :number) AND " +
                    "(e.status != -1) AND " +
                    "(:status IS NULL OR e.status = :status) AND " +
                    "(:movieId IS NULL OR e.movie.id = :movieId) ",
            nativeQuery = false)
    Page<Episode> findEpisodes(
            @Param("number") Integer number,
            @Param("status") Integer status,
            @Param("movieId") Integer movieId,
            Pageable pageable);

    Page<Episode> findByMovieIdAndStatusOrderByNumber(Integer movieId, Integer status, Pageable pageable);

    List<Episode> findByMovieIdAndStatus(Integer movieId, Integer status);
    List<Episode> findByMovieIdAndStatusOrderByNumberAsc(Integer movieId, Integer status);
    Optional<Episode> findBySlugAndStatus(String slug, Integer status);

    Optional<Episode> findBySlug(String slug);
    List<Episode> findAllByNumberAndMovieId(Integer episodeNumber, Integer movieId);
    Episode findByMovieIdAndNumberAndStatus(Integer movieId,Integer number,Integer status);
}
