package com.defty.movie.controller.user;

import com.defty.movie.dto.response.MovieDetailResponse;
import com.defty.movie.service.IEpisodeService;
import com.defty.movie.service.IEpisodeUserService;
import com.defty.movie.service.IMovieUserService;
import com.defty.movie.utils.ApiResponeUtil;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/user/accessible/episode")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EpisodeUserController {

    IEpisodeUserService episodeUserService;

    @GetMapping
    public Object getEpisodeDetail(@RequestParam String slug) {
        MovieDetailResponse movieDetailResponse = episodeUserService.getEpispdeDetails(slug);
        return ApiResponeUtil.ResponseOK(movieDetailResponse);
    }

    @GetMapping("/list")
    public Object getListEpisode(@RequestParam(value = "slugEpisode") String slugEpisode,
                                 @Valid @RequestParam(value = "page", defaultValue = "0") int page,
                                 @RequestParam(value = "size", defaultValue = "10") int size) {
        MovieDetailResponse movieDetailResponse = episodeUserService.getEpispdeDetails(slugEpisode);
        return ApiResponeUtil.ResponseOK(movieDetailResponse);
    }
}
