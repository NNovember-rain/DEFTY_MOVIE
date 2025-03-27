package com.defty.movie.controller.user;

import com.defty.movie.dto.response.MovieSearchResultResponse;
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

}
