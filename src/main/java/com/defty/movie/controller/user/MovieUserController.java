package com.defty.movie.controller.user;

import com.defty.movie.dto.response.*;
import com.defty.movie.service.IMovieUserRedisService;
import com.defty.movie.service.IMovieUserService;
import com.defty.movie.utils.ApiResponeUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/user/accessible/movie")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MovieUserController {

    IMovieUserService movieUserService;
    IMovieUserRedisService movieUserRedisService;


    @GetMapping("/movie-search/result")
    public Object getMovies(@RequestParam String title) {
        List<MovieSearchResultResponse> movieDetailResponse = movieUserService.getMovies(title);
        return ApiResponeUtil.ResponseOK(movieDetailResponse);
    }

    @GetMapping("/movie-search")
    public Object findByMovieName(@RequestParam String title) throws JsonProcessingException {
            List<MovieRedisDTO> movieNameResponseList = movieUserRedisService.getAllMovie(title);
        return ApiResponeUtil.ResponseOK(movieNameResponseList);
    }

    @GetMapping()
    public Object getMovieDetail(@RequestParam(value = "slugMovie") String slugMovie) {
        MovieDetailResponse movieDetailResponse = movieUserService.getMovieDetails(slugMovie);
        return ApiResponeUtil.ResponseOK(movieDetailResponse);
    }

    @GetMapping("/episode")
    public Object getMovieEpisodeDetail(@RequestParam(value = "slugMovie") String slugMovie,
                                        @Valid @RequestParam(value = "page", defaultValue = "0") int page,
                                        @RequestParam(value = "size", defaultValue = "10") int size)
    {
        Pageable pageable = PageRequest.of(page, size);
        PageableResponse<EpisodeResponse> episodeResponses = movieUserService.getEpisodes(slugMovie,pageable);
        return ApiResponeUtil.ResponseOK(episodeResponses);
    }

    @GetMapping("/actor")
    public Object getMovieActorDetail(@RequestParam(value = "slugMovie") String slugMovie) {
        MovieDetailDirectorActorResponse episodeResponses = movieUserService.getMovieDetailActor(slugMovie);
        return ApiResponeUtil.ResponseOK(episodeResponses);
    }
}
