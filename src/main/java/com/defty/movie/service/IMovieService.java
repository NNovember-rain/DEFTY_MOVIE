package com.defty.movie.service;

import com.defty.movie.dto.request.MovieRequest;
import com.defty.movie.dto.response.ApiResponse;
import com.defty.movie.dto.response.MovieResponse;
import com.defty.movie.dto.response.PageableResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IMovieService {
    ApiResponse<Integer> addMovie(MovieRequest movieRequest);
    ApiResponse<PageableResponse<MovieResponse>> getMovies(Pageable pageable, String title, String nation, String releaseDate, Integer ranking, Integer directorId, Integer status);
    ApiResponse<Integer> updateMovie(Integer id, MovieRequest movieRequest);
    ApiResponse<List<Integer>> deleteMovie(List<Integer> ids);
    ApiResponse<Integer> changeStatus(Integer id);
    Object getMovie(Integer id);
}
