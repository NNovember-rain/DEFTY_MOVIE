package com.defty.movie.repository;

import com.defty.movie.entity.MovieComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IMovieCommentRepository extends JpaRepository<MovieComment, Integer> {
    Optional<List<MovieComment>> findByParentMovieComment_Id(Integer movieCommentId);
    Page<MovieComment> findByEpisode_IdAndStatus(Integer episodeId, int status, Pageable pageable);
    Optional<MovieComment> findByIdAndStatus(Integer id, int status);
    Optional<List<MovieComment>> findByParentMovieCommentIdAndStatus(Integer movieCommentId, int status);
    Page<MovieComment> findByEpisodeIdAndParentMovieCommentIsNullAndStatus(Integer episodeId, int status, Pageable pageable);
}
