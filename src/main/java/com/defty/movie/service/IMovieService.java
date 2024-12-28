package com.defty.movie.service;

import com.defty.movie.dto.request.MovieRequest;
import com.defty.movie.dto.response.ApiResponse;
import com.defty.movie.dto.response.MovieResponse;
import com.defty.movie.dto.response.PageableResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.Date;
import java.util.List;

public interface IMovieService {
    ApiResponse<Integer> addMovie(MovieRequest movieRequest);
    ApiResponse<PageableResponse<MovieResponse>> getMovies(Pageable pageable, String title, String nation, String releaseDate, Integer ranking, Integer directorId, Integer status);
    ApiResponse<Integer> updateMovie(Integer id, MovieRequest movieRequest);
    ApiResponse<List<Integer>> deleteMovie(List<Integer> ids);
    ApiResponse<List<Integer>> disableMovie(List<Integer> ids);
    ApiResponse<List<Integer>> enableMovie(List<Integer> ids);
    Object getMovie(Integer id);
}
