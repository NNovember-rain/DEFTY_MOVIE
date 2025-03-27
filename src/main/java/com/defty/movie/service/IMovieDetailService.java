package com.defty.movie.service;

import com.defty.movie.dto.response.EpisodeResponse;
import com.defty.movie.dto.response.MovieDetailDirectorActorResponse;
import com.defty.movie.dto.response.MovieDetailResponse;
import com.defty.movie.dto.response.PageableResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IMovieDetailService {

    MovieDetailResponse getMovieDetails(String slugMovie);
    PageableResponse<EpisodeResponse> getEpisodes(String slugMovie, Pageable pageable);
    MovieDetailDirectorActorResponse getMovieDetailActor(String slugMovie);
}
