package com.defty.movie.service;

import com.defty.movie.dto.request.MovieRequest;
import com.defty.movie.dto.response.MovieResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IMovieService {
    ResponseEntity<String> addMovie(MovieRequest movieRequest);
    List<MovieResponse> getMovies();
    ResponseEntity<String> updateMovie(Integer id, MovieRequest movieRequest);
    ResponseEntity<String> deleteMovie(List<Integer> ids);
    Object getMovie(Integer id);
}
