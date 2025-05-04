package com.defty.movie.controller.admin;

import com.defty.movie.dto.request.MovieRequest;
import com.defty.movie.dto.response.ApiResponse;
import com.defty.movie.service.IMovieService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/admin/movie")
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
                            @RequestParam(name = "nationality", required = false) String nation,
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
    @PatchMapping("/status/{id}")
    @PreAuthorize("@requiredPermission.checkPermission('CHANGE_MOVIE_STATUS')")
    public ApiResponse<Integer> changeStatus(@PathVariable Integer id) {
        return movieService.changeStatus(id);
    }

    @PostMapping("/{movieId}/{actorIds}")
    @PreAuthorize("@requiredPermission.checkPermission('ADD_ACTOR_TO_MOVIE')")
    public ApiResponse<Integer> addActor(@PathVariable Integer movieId, @PathVariable List<Integer> actorIds) {
        return movieService.addActor(movieId, actorIds);
    }

    @GetMapping("/{movieId}/actors")
    @PreAuthorize("@requiredPermission.checkPermission('GET_ACTOR_BY_MOVIE')")
    public Object getActorsByMovie(Pageable pageable,
                                      @PathVariable Integer movieId,
                                      @RequestParam(name = "name", required = false) String name,
                                      @RequestParam(name = "gender", required = false) String gender,
                                      @RequestParam(name = "date_of_birth", required = false) String date_of_birth,
                                      @RequestParam(name = "nationality", required = false) String nationality) {
        return movieService.findActorsByMovie(pageable, movieId, name, gender, date_of_birth, nationality);
    }

    @DeleteMapping("/{movieId}/{actorIds}")
    @PreAuthorize("@requiredPermission.checkPermission('DELETE_ACTOR_FROM_MOVIE')")
    public ApiResponse<Integer> deleteActor(@PathVariable Integer movieId, @PathVariable List<Integer> actorIds) {
        return movieService.deleteActor(movieId, actorIds);
    }

    @GetMapping("/{movieId}/other-actors")
    @PreAuthorize("@requiredPermission.checkPermission('GET_ACTOR_NOT_IN_MOVIE')")
    public Object getActorsNotInMovie(Pageable pageable,
                                      @PathVariable Integer movieId,
                                      @RequestParam(name = "name", required = false) String name,
                                      @RequestParam(name = "gender", required = false) String gender,
                                      @RequestParam(name = "date_of_birth", required = false) String date_of_birth,
                                      @RequestParam(name = "nationality", required = false) String nationality) {
        return movieService.findActorsNotInMovie(pageable, movieId, name, gender, date_of_birth, nationality);
    }
}
