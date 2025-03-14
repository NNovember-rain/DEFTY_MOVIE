package com.defty.movie.service;

import com.defty.movie.dto.response.EpisodeResponse;
import com.defty.movie.dto.response.MovieDetailDirectorActorResponse;
import com.defty.movie.dto.response.MovieDetailResponse;

import java.util.List;

public interface IMovieDetailService {

    MovieDetailResponse getMovieDetails(String slugMovie);
    List<EpisodeResponse>  getEpisodes(String slugMovie);
    MovieDetailDirectorActorResponse getMovieDetailActor(String slugMovie);
}
