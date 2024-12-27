package com.defty.movie.controller.admin;

import com.defty.movie.dto.request.MovieRequest;
import com.defty.movie.dto.response.ApiResponse;
import com.defty.movie.dto.response.MovieResponse;
import com.defty.movie.service.IMovieService;
import com.defty.movie.service.impl.MovieService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/admin/movie")
//@RequestMapping("/movie")
public class MovieController {

    private final IMovieService movieService;

    @PostMapping("")
    @PreAuthorize("@requiredPermission.checkPermission('CREATE_MOVIE')")
    public ApiResponse<Integer> addMovie(@ModelAttribute MovieRequest movieRequest) {
         return movieService.addMovie(movieRequest);
    }

    @GetMapping("/{id}")
    @PreAuthorize("@requiredPermission.checkPermission('GET_MOVIE')")
    public Object getMovie(@PathVariable Integer id){
        return movieService.getMovie(id);
    }

    @GetMapping("")
    @PreAuthorize("@requiredPermission.checkPermission('GET_MOVIES')")
    public Object getMovies(Pageable pageable,
                            @RequestParam(name = "title", required = false) String title,
                            @RequestParam(name = "nation", required = false) String nation,
                            @RequestParam(name = "releaseDate", required = false) String releaseDate,
                            @RequestParam(name = "ranking", required = false) Integer ranking,
                            @RequestParam(name = "directorId", required = false) Integer directorId,
                            @RequestParam(name = "status", required = false) Integer status){
        return movieService.getMovies(pageable, title, nation, releaseDate, ranking, directorId, status);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("@requiredPermission.checkPermission('UPDATE_MOVIE')")
    public ApiResponse<Integer> patchMovie(@PathVariable Integer id, @ModelAttribute MovieRequest movieRequest) {
        return movieService.updateMovie(id, movieRequest);
    }

    @DeleteMapping("/{ids}")
    @PreAuthorize("@requiredPermission.checkPermission('DELETE_MOVIE')")
    public ApiResponse<List<Integer>> deleteMovie(@PathVariable List<Integer> ids) {
        return movieService.deleteMovie(ids);
    }
}
