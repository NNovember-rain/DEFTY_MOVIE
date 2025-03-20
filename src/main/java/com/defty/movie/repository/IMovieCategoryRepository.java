package com.defty.movie.repository;

import com.defty.movie.entity.Movie;
import com.defty.movie.entity.MovieCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IMovieCategoryRepository extends JpaRepository<MovieCategory, Integer> {
    MovieCategory findByCategoryIdAndMovieId(Integer categoryId, Integer MovieId);
    MovieCategory findByCategoryId(Integer categoryId);

    void deleteByCategoryIdAndMovieId(Integer categoryId, Integer movieId);

}
