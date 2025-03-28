package com.defty.movie.service;

import com.defty.movie.dto.response.MovieNameResponse;

import com.defty.movie.dto.response.MovieSearchResultResponse;

import java.util.List;

public interface IMovieUserService {
    List<MovieSearchResultResponse> getMovies(String title);
    List<MovieNameResponse> getAllMovie(String title);
//    List<MovieNameResponse> getHistoryMovieSearch(HttpServletRequest request);
}
