package com.defty.movie.controller.admin;

import com.defty.movie.dto.request.MovieRequest;
import com.defty.movie.dto.response.MovieResponse;
import com.defty.movie.service.impl.MovieService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/admin/movie")
//@RequestMapping("/movie")
public class MovieController {

    private final MovieService movieService;

    @PostMapping("")
    @PreAuthorize("@requiredPermission.checkPermission('CREATE_MOVIE')")
    public ResponseEntity<String> addMovie(@RequestBody MovieRequest movieRequest) {
         return movieService.addMovie(movieRequest);
    }

    @GetMapping("/{id}")
    @PreAuthorize("@requiredPermission.checkPermission('GET_MOVIE')")
    public Object getMovie(@PathVariable Integer id){
        return movieService.getMovie(id);
    }

    @GetMapping("")
    @PreAuthorize("@requiredPermission.checkPermission('GET_MOVIES')")
    public List<MovieResponse> getMovies(){
        return movieService.getMovies();
    }

    @PatchMapping("/{id}")
    @PreAuthorize("@requiredPermission.checkPermission('UPDATE_MOVIE')")
    public ResponseEntity<String> patchMovie(@PathVariable Integer id, @RequestBody MovieRequest movieRequest) {
        return movieService.updateMovie(id, movieRequest);
    }

    @DeleteMapping("/{ids}")
    @PreAuthorize("@requiredPermission.checkPermission('DELETE_MOVIE')")
    public ResponseEntity<String> deleteMovie(@PathVariable List<Integer> ids) {
        return movieService.deleteMovie(ids);
    }
}
