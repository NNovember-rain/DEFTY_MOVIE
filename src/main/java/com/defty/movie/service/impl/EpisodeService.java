package com.defty.movie.service.impl;


import com.defty.movie.entity.Movie;
import com.defty.movie.exception.AlreadyExitException;
import com.defty.movie.exception.MediaUploadException;
import com.defty.movie.exception.NotFoundException;
import com.defty.movie.mapper.EpisodeMapper;
import com.defty.movie.dto.request.EpisodeRequest;
import com.defty.movie.dto.response.ApiResponse;
import com.defty.movie.dto.response.EpisodeResponse;
import com.defty.movie.dto.response.PageableResponse;
import com.defty.movie.entity.Episode;
import com.defty.movie.repository.IEpisodeRepository;
import com.defty.movie.repository.IMovieRepository;
import com.defty.movie.service.IEpisodeService;
import com.defty.movie.utils.CopyUtil;
import com.defty.movie.utils.UploadImageUtil;
import com.defty.movie.utils.UploadVideoUtil;
import com.defty.movie.validation.EpisodeValidation;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EpisodeService implements IEpisodeService {
    EpisodeMapper episodeMapper;
    IEpisodeRepository episodeRepository;
    EpisodeValidation episodeValidation;
    UploadImageUtil uploadImageUtil;
    UploadVideoUtil uploadVideoUtil;
    IMovieRepository movieRepository;

    @Override
    public ApiResponse<Integer> addEpisode(EpisodeRequest episodeRequest) {
        episodeValidation.fieldValidation(episodeRequest);
        List<Episode> episodes = episodeRepository.findAllByNumber(episodeRequest.getNumber());
        if (!episodes.isEmpty()){
            throw new AlreadyExitException("Episode number already exists");

        }
        Episode episode = episodeMapper.toEpisodeEntity(episodeRequest);
        Optional<Movie> movie = movieRepository.findById(episodeRequest.getMovieId());
        if(movie.isPresent()){
            episode.setMovie(movie.get());
            if(movie.get().getSlug() != null){
                episode.setSlug(movie.get().getSlug() + episode.getNumber());
            }
        }
        else {
            throw new NotFoundException("Movie not found exception");
        }
        if (episodeRequest.getThumbnail() != null && !episodeRequest.getThumbnail().isEmpty()) {
            try{
                episode.setThumbnail(uploadImageUtil.upload(episodeRequest.getThumbnail()));
            }
            catch(Exception e){
                throw new MediaUploadException("Could not upload the image, please try again later!");
            }
        }
        else {
            episode.setThumbnail(null);
        }
        if(episodeRequest.getLink() != null && !episodeRequest.getLink().isEmpty()) {
            try {
                episode.setLink(uploadVideoUtil.upload(episodeRequest.getLink()));
            } catch (Exception e) {
                throw new MediaUploadException("Could not upload the video, please try again later! " + e);
            }
        }
        else{
            episode.setLink(null);
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
            List<Episode> episodes = episodeRepository.findAllByNumber(episodeRequest.getNumber());
            if (!episodes.isEmpty() && updatedEpisode.getNumber() != episodeRequest.getNumber()){
                throw new AlreadyExitException("Episode number already exists");

            }
            updatedEpisode.setSlug(updatedEpisode.getMovie().getSlug() + updatedEpisode.getNumber());
            /*copy different fields from episodeRequest to updatedEpisode*/
            CopyUtil.copyPropertiesIgnoreNull(episodeRequest, updatedEpisode);
            if (episodeRequest.getThumbnail() != null && !episodeRequest.getThumbnail().isEmpty()) {
                try{
                    updatedEpisode.setThumbnail(uploadImageUtil.upload(episodeRequest.getThumbnail()));
                }
                catch(Exception e){
                    throw new MediaUploadException("Could not upload the image, please try again later!");
                }
            }
            if(episodeRequest.getLink() != null && !episodeRequest.getLink().isEmpty()) {
                try {
                    updatedEpisode.setLink(uploadVideoUtil.upload(episodeRequest.getLink()));
                } catch (Exception e) {
                    throw new MediaUploadException("Could not upload the video, please try again later! " + e);
                }
            }
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
            episode.setStatus(-1);
        }
        episodeRepository.saveAll(episodeEntity);
        if(ids.size() > 1){
            return new ApiResponse<>(200, "Delete episodes successfully", ids);
        }
        return new ApiResponse<>(200, "Delete episode successfully", ids);

    }

    @Override
    public ApiResponse<Integer> changeStatus(Integer id) {
        Optional<Episode> episode = episodeRepository.findById(id);
        if(episode.get() != null){
            String message = "";
            if(episode.get().getStatus() == 0){
                episode.get().setStatus(1);
                message += "Enable episodes successfully";
            }
            else{
                episode.get().setStatus(0);
                message += "Disable episodes successfully";
            }
            episodeRepository.save(episode.get());
            return new ApiResponse<>(200, message, id);
        }
        else throw new NotFoundException("Not found exception");
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
