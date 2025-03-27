package com.defty.movie.controller.user;

import com.defty.movie.service.ICategoryService;
import com.defty.movie.service.IMovieService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/user/accessible/movie")
public class MovieUserController {
    private final IMovieService movieService;
//    @GetMapping("/episode/{id}")
//    public Object getEpisodeOfMovieDetails() {
//        return showonService.getAllShowons(pageable, contentType,contentName, status);
//    }
}
