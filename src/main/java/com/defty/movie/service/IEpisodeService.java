package com.defty.movie.service;

import com.defty.movie.dto.request.EpisodeRequest;
import com.defty.movie.dto.response.ApiResponse;
import com.defty.movie.dto.response.EpisodeResponse;
import com.defty.movie.dto.response.PageableResponse;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IEpisodeService {
    ApiResponse<Integer> addEpisode(EpisodeRequest movieRequest);
    ApiResponse<PageableResponse<EpisodeResponse>> getEpisodes(Pageable pageable, Integer number, Integer status, Integer movieId);
    ApiResponse<Integer> updateEpisode(Integer id, EpisodeRequest movieRequest);
    ApiResponse<List<Integer>> deleteEpisode(List<Integer> ids);
    ApiResponse<List<Integer>> disableEpisode(List<Integer> ids);
    ApiResponse<List<Integer>> enableEpisode(List<Integer> ids);
    Object getEpisode(Integer id);
}
