package com.defty.movie.service;

import com.defty.movie.dto.response.*;
import com.defty.movie.entity.Movie;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IMovieUserService {
    List<MovieSearchResultResponse> getMovies(String title);
    List<MovieNameResponse> getAllMovie(String title);
    MovieDetailResponse getMovieDetails(String slugMovie);
    PageableResponse<EpisodeResponse> getEpisodes(String slugMovie, Pageable pageable);
    MovieDetailDirectorActorResponse getMovieDetailActor(String slugMovie);
    Movie getMovieBySlugEpisode(String slugEpisode);
    List<MovieNameResponse> getAllMovies();
}
