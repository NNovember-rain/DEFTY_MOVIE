package com.defty.movie.repository;

import com.defty.movie.entity.MovieComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IMovieCommentRepository extends JpaRepository<MovieComment, Integer> {
    Optional<List<MovieComment>> findByParentMovieComment_Id(Integer movieCommentId);
    Optional<List<MovieComment>> findByEpisode_IdAndStatus(Integer episodeId, int status);
}
