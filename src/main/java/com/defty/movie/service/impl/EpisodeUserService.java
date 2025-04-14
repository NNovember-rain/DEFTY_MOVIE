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
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EpisodeUserService implements IEpisodeUserService {

    IEpisodeRepository episodeRepository;
    IMovieUserService movieUserService;

    @Override
    public MovieDetailResponse getEpispdeDetails(String slug) {
        Movie movie=movieUserService.getMovieBySlugEpisode(slug);
        MovieDetailResponse movieDetailResponse=movieUserService.getMovieDetails(movie.getSlug());
        return movieDetailResponse;
    }

    @Override
    public PageableResponse<EpisodeResponse> getEpisodes(String slugEpisode, Pageable pageable) {
        Movie movie=movieUserService.getMovieBySlugEpisode(slugEpisode);
        List<EpisodeResponse> episodeResponses=new ArrayList<>();
        for(Episode episode:movie.getEpisodes()){
            EpisodeResponse episodeResponse=new EpisodeResponse();
            BeanUtils.copyProperties(episode,episodeResponse);
            episodeResponses.add(episodeResponse);
        }
        int totalElements=episodeRepository.findByMovieIdAndStatus(movie.getId(),1).size();

        PageableResponse<EpisodeResponse> pageableResponse=new PageableResponse<>();
        pageableResponse.setContent(episodeResponses);
        pageableResponse.setTotalElements(0L+totalElements);
        return pageableResponse;

    }
}
