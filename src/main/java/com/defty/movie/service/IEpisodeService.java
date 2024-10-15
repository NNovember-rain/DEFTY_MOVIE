package com.defty.movie.service;

import com.defty.movie.dto.request.EpisodeRequest;
import com.defty.movie.dto.response.EpisodeResponseDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IEpisodeService {
    ResponseEntity<String> addEpisode(EpisodeRequest movieRequest);
    List<EpisodeResponseDTO> getEpisodes();
    ResponseEntity<String> updateEpisode(Integer id, EpisodeRequest movieRequest);
    ResponseEntity<String> deleteEpisode(List<Integer> ids);
    Object getEpisode(Integer id);
}
