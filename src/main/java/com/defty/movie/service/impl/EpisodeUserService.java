package com.defty.movie.service.impl;

import com.defty.movie.dto.response.EpisodeResponse;
import com.defty.movie.dto.response.MovieDetailResponse;
import com.defty.movie.dto.response.PageableResponse;
import com.defty.movie.entity.Episode;
import com.defty.movie.entity.Movie;
import com.defty.movie.exception.NotFoundException;
import com.defty.movie.repository.IEpisodeRepository;
import com.defty.movie.repository.IMovieRepository;
import com.defty.movie.service.IEpisodeUserService;
import com.defty.movie.service.IMovieUserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EpisodeUserService implements IEpisodeUserService {

    IEpisodeRepository episodeRepository;
    IMovieUserService movieUserService;
    IMovieRepository movieRepository;

    @Override
    public MovieDetailResponse getEpispdeDetails(String slug) {
        Movie movie=movieUserService.getMovieBySlugEpisode(slug);
        MovieDetailResponse movieDetailResponse=movieUserService.getMovieDetails(movie.getSlug());
        return movieDetailResponse;
    }

    @Override
    public MovieDetailResponse getEpisodeFirst(String slugMovie) {
        return movieUserService.getMovieDetails(slugMovie);
    }

    @Override
    public PageableResponse<EpisodeResponse> getEpisodes(String slugEpisode, Pageable pageable) {
        Movie movie=movieUserService.getMovieBySlugEpisode(slugEpisode);
        PageableResponse<EpisodeResponse> episodes=movieUserService.getEpisodes(movie.getSlug(),pageable);
        int totalElements=episodeRepository.findByMovieIdAndStatus(movie.getId(),1).size();
        episodes.setTotalElements(totalElements+0L);

        return episodes;
    }

    @Override
    public EpisodeResponse getVideoUrl(String slug) {
        Optional<Episode> episode=episodeRepository.findBySlugAndStatus(slug,1);
        if(episode.isPresent()){
            EpisodeResponse episodeResponse=new EpisodeResponse();
            BeanUtils.copyProperties(episode.get(),episodeResponse);
            return episodeResponse;
        }
        else throw new NotFoundException("Episode not found");
    }

    @Override
    public EpisodeResponse getVideoUrlFirst(String slugMovie) {
        Movie movie = movieRepository.findByStatusAndSlug(1, slugMovie);
        List<Episode> episodeList = episodeRepository.findByMovieIdAndStatus(movie.getId(), 1);
        if (!episodeList.isEmpty()){
            Episode episode = episodeList.get(0);
            EpisodeResponse episodeResponse=new EpisodeResponse();
            BeanUtils.copyProperties(episode,episodeResponse);
            return episodeResponse;
        }
        else {
            log.error("Movie has not episode");
            throw new RuntimeException("Movie has not episode");
        }
    }
}
