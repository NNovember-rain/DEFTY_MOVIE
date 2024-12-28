package com.defty.movie.repository;

import com.defty.movie.entity.Episode;
import com.defty.movie.entity.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public interface IEpisodeRepository extends JpaRepository<Episode, Integer> {
    @Query(value = "SELECT e FROM Episode e WHERE " +
            "(:number IS NULL OR e.number = :number) AND " +
            "(:status IS NULL OR e.status = :status) AND " +
            "(e.status != -1) AND " +
            "(:movieId IS NULL OR e.movie.id = :movieId) " +
            "ORDER BY e.createdDate DESC",
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
}
