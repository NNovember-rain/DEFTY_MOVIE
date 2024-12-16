package com.defty.movie.service;

import com.defty.movie.dto.request.MovieRequest;
import com.defty.movie.dto.response.MovieResponse;
import com.defty.movie.dto.response.PageableResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.Date;
import java.util.List;

public interface IMovieService {
    ResponseEntity<String> addMovie(MovieRequest movieRequest);
    PageableResponse<MovieResponse> getMovies(Pageable pageable, String title, String nation, String releaseDate, Integer ranking, Integer directorId, Integer status);
    ResponseEntity<String> updateMovie(Integer id, MovieRequest movieRequest);
    ResponseEntity<String> deleteMovie(List<Integer> ids);
    Object getMovie(Integer id);
}
