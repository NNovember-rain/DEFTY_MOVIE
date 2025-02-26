package com.defty.movie.service;

import com.defty.movie.dto.response.EpisodeResponse;
import com.defty.movie.dto.response.MovieDetailActorResponse;
import com.defty.movie.dto.response.MovieDetailResponse;

import java.util.List;

public interface IMovieDetailService {
    MovieDetailResponse getMovieDetails(Integer movieId);
    List<EpisodeResponse>  getEpisodes(Integer movieId);
    MovieDetailActorResponse getMovieDetailActor(Integer movieId);
}
