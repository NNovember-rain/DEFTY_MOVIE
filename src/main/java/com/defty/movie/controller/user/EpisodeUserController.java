package com.defty.movie.controller.user;

import com.defty.movie.dto.response.EpisodeResponse;
import com.defty.movie.dto.response.MovieDetailResponse;
import com.defty.movie.dto.response.PageableResponse;
import com.defty.movie.service.IEpisodeService;
import com.defty.movie.service.IEpisodeUserService;
import com.defty.movie.service.IMovieUserService;
import com.defty.movie.utils.ApiResponeUtil;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
        MovieDetailResponse movieDetailResponse = episodeUserService.getEpisodeDetails(slug);
        return ApiResponeUtil.ResponseOK(movieDetailResponse);
    }

    @GetMapping("/first")
    public Object getEpisodeFirst(@RequestParam String slug) { //slugMovie
        MovieDetailResponse movieDetailResponse = episodeUserService.getEpisodeFirst(slug);
        return ApiResponeUtil.ResponseOK(movieDetailResponse);
    }

    @GetMapping("/list")
    public Object getListEpisode(@RequestParam(value = "slug") String slug,
                                 @Valid @RequestParam(value = "page", defaultValue = "0") int page,
                                 @RequestParam(value = "size", defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("number").ascending());
        PageableResponse<EpisodeResponse> episodes = episodeUserService.getEpisodes(slug,pageable);
        return ApiResponeUtil.ResponseOK(episodes);
    }

    @GetMapping("/video")
    public Object getVideoEpisode(@RequestParam String slug) {
        EpisodeResponse data=episodeUserService.getVideoUrl(slug);
        return ApiResponeUtil.ResponseOK(data);
    }

    @GetMapping("/first/video")
    public Object getVideoEpisodeFirst(@RequestParam String slug) { // slugMovie
        EpisodeResponse data = episodeUserService.getVideoUrlFirst(slug);
        return ApiResponeUtil.ResponseOK(data);
    }
}
