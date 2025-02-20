package com.defty.movie.repository;


import com.defty.movie.entity.MovieCommentReaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IMovieCommentReactionRepository extends JpaRepository<MovieCommentReaction, Integer> {
    List<MovieCommentReaction> findByMovieCommentId(Integer movieCommentId);
}
