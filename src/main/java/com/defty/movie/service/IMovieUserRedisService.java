package com.defty.movie.service;

import com.defty.movie.dto.response.MovieRedisDTO;
import com.defty.movie.entity.Movie;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;

public interface IMovieUserRedisService {
    List<MovieRedisDTO> getAllMovie(String title) throws JsonProcessingException;
    void saveAllMovie(List<Movie> movies) throws JsonProcessingException;
    void clearCache();
    boolean hasMovieData();
}
