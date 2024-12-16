package com.defty.movie.service.impl;

import com.defty.movie.dto.request.EpisodeRequest;
import com.defty.movie.dto.response.EpisodeResponse;
import com.defty.movie.dto.response.PageableResponse;
import com.defty.movie.entity.Episode;
import com.defty.movie.exception.NotFoundException;
import com.defty.movie.mapper.EpisodeMapper;
import com.defty.movie.repository.IEpisodeRepository;
import com.defty.movie.service.IEpisodeService;
import com.defty.movie.validation.EpisodeValidation;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EpisodeService implements IEpisodeService {
    private final EpisodeMapper episodeMapper;
    private final IEpisodeRepository episodeRepository;
    private final EpisodeValidation episodeValidation;
    @Override
    public ResponseEntity<String> addEpisode(EpisodeRequest episodeRequest) {
        episodeValidation.fieldValidation(episodeRequest);

        Episode episode = episodeMapper.toEpisodeEntity(episodeRequest);
        episodeRepository.save(episode);
        return ResponseEntity.ok("Add episode successfully");
    }

    public PageableResponse<EpisodeResponse> getEpisodes(Pageable pageable, Integer number, Integer status, Integer movieId) {
        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("createdDate").descending());
        Page<Episode> episodes = episodeRepository.findEpisodes(number, status, movieId, sortedPageable);
        List<EpisodeResponse> episodeResponseDTOS = new ArrayList<>();
        if (episodes.isEmpty()){
            throw new NotFoundException("Not found exception");
        }
        else {
            for(Episode m : episodes){
                episodeResponseDTOS.add(episodeMapper.toEpisodeResponseDTO(m));
            }

            PageableResponse<EpisodeResponse> pageableResponse = new PageableResponse<>(episodeResponseDTOS, episodeRepository.count());
            return pageableResponse;
        }
    }

    @Override
    public ResponseEntity<String> updateEpisode(Integer id, EpisodeRequest episodeRequest) {
        episodeValidation.fieldValidation(episodeRequest);
        Optional<Episode> episode = episodeRepository.findById(id);
        if(episode.isPresent()){
            Episode updatedEpisode = episode.get();
            /*copy different fields from episodeRequest to updatedEpisode*/
            BeanUtils.copyProperties(episodeRequest, updatedEpisode, "id");
            episodeRepository.save(updatedEpisode);
        }
        else {
            throw new NotFoundException("Not found exception");
        }
        return ResponseEntity.ok("Update episode successfully");
    }

    @Override
    public ResponseEntity<String> deleteEpisode(List<Integer> ids) {
        List<Episode> episodeEntity = episodeRepository.findAllById(ids);
        if(episodeEntity.size() == 0) throw new NotFoundException("Not found exception");
        for(Episode episode : episodeEntity){
            episode.setStatus(0);
        }
        episodeRepository.saveAll(episodeEntity);
        if(ids.size() > 1){
            return ResponseEntity.ok("Update episodes successfully");
        }
        return ResponseEntity.ok("Update episode successfully");
    }

    @Override
    public Object getEpisode(Integer id) {
        Optional<Episode> movie = episodeRepository.findById(id);
        if(movie.isPresent()){
            return episodeMapper.toEpisodeResponseDTO(movie.get());
        }
        return "Episode doesn't exist";
    }
}
