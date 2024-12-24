package com.defty.movie.service.impl;

import com.defty.movie.dto.request.EpisodeRequest;
import com.defty.movie.dto.response.ApiResponse;
import com.defty.movie.dto.response.CategoryResponse;
import com.defty.movie.dto.response.EpisodeResponse;
import com.defty.movie.dto.response.PageableResponse;
import com.defty.movie.entity.Episode;
import com.defty.movie.exception.ImageUploadException;
import com.defty.movie.exception.NotFoundException;
import com.defty.movie.mapper.EpisodeMapper;
import com.defty.movie.repository.IEpisodeRepository;
import com.defty.movie.service.IEpisodeService;
import com.defty.movie.utils.UploadImageUtil;
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
    EpisodeMapper episodeMapper;
    IEpisodeRepository episodeRepository;
    EpisodeValidation episodeValidation;
    UploadImageUtil uploadImageUtil;
    @Override
    public ApiResponse<Integer> addEpisode(EpisodeRequest episodeRequest) {
        episodeValidation.fieldValidation(episodeRequest);

        Episode episode = episodeMapper.toEpisodeEntity(episodeRequest);
        try{
            episode.setThumbnail(uploadImageUtil.upload(episodeRequest.getThumbnail()));
        }
        catch(Exception e){
            throw new ImageUploadException("Could not upload the image, please try again later!");
        }
        try{
            episodeRepository.save(episode);
        }
        catch (Exception e){
            return new ApiResponse<>(500, e.getMessage(), episode.getId());
        }
        return new ApiResponse<>(201, "created", episode.getId());
    }

    public ApiResponse<PageableResponse<EpisodeResponse>> getEpisodes(Pageable pageable, Integer number, Integer status, Integer movieId) {
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

            PageableResponse<EpisodeResponse> pageableResponse = new PageableResponse<>(episodeResponseDTOS, episodes.getTotalElements());
            return new ApiResponse<>(200, "OK", pageableResponse);
        }
    }

    @Override
    public ApiResponse<Integer> updateEpisode(Integer id, EpisodeRequest episodeRequest) {
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
        return new ApiResponse<>(200, "Update episode successfully", id);
    }

    @Override
    public ApiResponse<List<Integer>> deleteEpisode(List<Integer> ids) {
        List<Episode> episodeEntity = episodeRepository.findAllById(ids);
        if(episodeEntity.size() == 0) throw new NotFoundException("Not found exception");
        for(Episode episode : episodeEntity){
            episode.setStatus(0);
        }
        episodeRepository.saveAll(episodeEntity);
        if(ids.size() > 1){
            return new ApiResponse<>(200, "Delete episodes successfully", ids);
        }
        return new ApiResponse<>(200, "Delete episode successfully", ids);

    }

    @Override
    public Object getEpisode(Integer id) {
        Optional<Episode> movie = episodeRepository.findById(id);
        if(movie.isPresent()){
            return new ApiResponse<>(200, "OK", episodeMapper.toEpisodeResponseDTO(movie.get()));
        }
        return new ApiResponse<>(404, "Episode doesn't exist", null);
    }
}
