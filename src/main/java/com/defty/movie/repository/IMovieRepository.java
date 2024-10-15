package com.defty.movie.repository;

import com.defty.movie.entity.MovieEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IMovieRepository extends JpaRepository<MovieEntity, Integer> {
}
