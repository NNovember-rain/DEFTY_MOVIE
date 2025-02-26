package com.defty.movie.controller.user;

import com.defty.movie.dto.response.EpisodeResponse;
import com.defty.movie.dto.response.MovieDetailActorResponse;
import com.defty.movie.dto.response.MovieDetailResponse;
import com.defty.movie.service.IMovieDetailService;
import com.defty.movie.service.IMovieService;
import com.defty.movie.utils.ApiResponeUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/user/moviedetail")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MovieDetailController {

    private final IMovieDetailService movieDetailService;

    @GetMapping("/{movieid}")
    public Object getDetailMovie(@PathVariable Integer movieid) {
        MovieDetailResponse movieDetailResponse = movieDetailService.getMovieDetails(movieid);
        return ApiResponeUtil.ResponseOK(movieDetailResponse);
    }

    @GetMapping("/episode/{movieid}")
    public Object getDetailMovieEpisode(@PathVariable Integer movieid) {
        List<EpisodeResponse> episodeResponses = movieDetailService.getEpisodes(movieid);
        return ApiResponeUtil.ResponseOK(episodeResponses);
    }

    @GetMapping("/actor/{movieid}")
    public Object getDetailMovieActor(@PathVariable Integer movieid) {
        MovieDetailActorResponse episodeResponses = movieDetailService.getMovieDetailActor(movieid);
        return ApiResponeUtil.ResponseOK(episodeResponses);
    }
}
