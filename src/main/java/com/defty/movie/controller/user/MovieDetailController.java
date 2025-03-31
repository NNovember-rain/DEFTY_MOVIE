package com.defty.movie.controller.user;

import com.defty.movie.dto.response.*;
import com.defty.movie.service.IMovieDetailService;
import com.defty.movie.utils.ApiResponeUtil;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/user/accessible/moviedetail")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MovieDetailController {


    private final IMovieDetailService movieDetailService;

    @GetMapping()
    public Object getDetailMovie(@RequestParam(value = "slugMovie") String slugMovie) {
        MovieDetailResponse movieDetailResponse = movieDetailService.getMovieDetails(slugMovie);
        return ApiResponeUtil.ResponseOK(movieDetailResponse);
    }

    @GetMapping("/episode")
    public Object getDetailMovieEpisode(@RequestParam(value = "slugMovie") String slugMovie,
                                        @Valid @RequestParam(value = "page", defaultValue = "0") int page,
                                        @RequestParam(value = "size", defaultValue = "10") int size)
    {
        Pageable pageable = PageRequest.of(page, size);
        PageableResponse<EpisodeResponse> episodeResponses = movieDetailService.getEpisodes(slugMovie,pageable);
        return ApiResponeUtil.ResponseOK(episodeResponses);
    }

    @GetMapping("/actor")
    public Object getDetailMovieActor(@RequestParam(value = "slugMovie") String slugMovie) {
        MovieDetailDirectorActorResponse episodeResponses = movieDetailService.getMovieDetailActor(slugMovie);
        return ApiResponeUtil.ResponseOK(episodeResponses);
    }
}
