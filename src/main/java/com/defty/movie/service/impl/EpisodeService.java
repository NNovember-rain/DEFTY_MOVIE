package com.defty.movie.service.impl;

import com.defty.movie.dto.request.EpisodeRequest;
import com.defty.movie.dto.response.EpisodeResponseDTO;
import com.defty.movie.service.IEpisodeService;
import com.defty.movie.validation.EpisodeValidation;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EpisodeService implements IEpisodeService {
    private final EpisodeValidation episodeValidation;
    @Override
    public ResponseEntity<String> addEpisode(EpisodeRequest movieRequest) {
        episodeValidation.fieldValidation(movieRequest);
        return null;
    }

    @Override
    public List<EpisodeResponseDTO> getEpisodes() {
        return null;
    }

    @Override
    public ResponseEntity<String> updateEpisode(Integer id, EpisodeRequest movieRequest) {
        return null;
    }

    @Override
    public ResponseEntity<String> deleteEpisode(List<Integer> ids) {
        return null;
    }

    @Override
    public Object getEpisode(Integer id) {
        return null;
    }
}
