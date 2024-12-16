package com.defty.movie.service;

import com.defty.movie.dto.request.EpisodeRequest;
import com.defty.movie.dto.response.EpisodeResponse;
import com.defty.movie.dto.response.PageableResponse;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IEpisodeService {
    ResponseEntity<String> addEpisode(EpisodeRequest movieRequest);
    PageableResponse<EpisodeResponse> getEpisodes(Pageable pageable, Integer number, Integer status, Integer movieId);
    ResponseEntity<String> updateEpisode(Integer id, EpisodeRequest movieRequest);
    ResponseEntity<String> deleteEpisode(List<Integer> ids);
    Object getEpisode(Integer id);
}
