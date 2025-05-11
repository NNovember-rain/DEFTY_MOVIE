package com.defty.movie.service;

import com.defty.movie.dto.response.EpisodeResponse;
import com.defty.movie.dto.response.MovieDetailResponse;
import com.defty.movie.dto.response.PageableResponse;
import org.springframework.data.domain.Pageable;

public interface IEpisodeUserService {
    MovieDetailResponse getEpispdeDetails(String slug);
    MovieDetailResponse getEpisodeFirst(String slugMovie);
    PageableResponse<EpisodeResponse> getEpisodes(String slugEpisode, Pageable pageable);
    EpisodeResponse getVideoUrl(String slug);
    EpisodeResponse getVideoUrlFirst(String slugMovie);
}
