package com.defty.movie.repository;


import com.defty.movie.entity.MovieCommentReaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IMovieCommentReactionRepository extends JpaRepository<MovieCommentReaction, Integer> {
}
