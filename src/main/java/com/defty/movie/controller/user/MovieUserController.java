package com.defty.movie.controller.user;

import com.defty.movie.dto.response.MovieRedisDTO;
import com.defty.movie.dto.response.MovieSearchResultResponse;
import com.defty.movie.service.IMovieUserRedisService;
import com.defty.movie.service.IMovieUserService;
import com.defty.movie.utils.ApiResponeUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
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

    // API tìm kiếm phim theo tên
    @GetMapping("/movie-search")
    public Object findByMovieName(@RequestParam String title) throws JsonProcessingException {
            List<MovieRedisDTO> movieNameResponseList = movieUserRedisService.getAllMovie(title);
        return ApiResponeUtil.ResponseOK(movieNameResponseList);
    }

}
