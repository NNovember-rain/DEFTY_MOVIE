package com.defty.movie.controller.user;

import com.defty.movie.dto.request.MovieCommentRequest;
import com.defty.movie.dto.request.MovieCommentRequest;
import com.defty.movie.dto.response.MovieCommentResponse;
import com.defty.movie.entity.MovieComment;
import com.defty.movie.service.IMovieCommentService;
import com.defty.movie.utils.ApiResponeUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/user/movie-comment")
public class MovieCommentController {

    private final IMovieCommentService movieCommentService;
    String PREFIX_MOVIE_COMMENT = "MOVIE_COMMENT | ";

    @PostMapping()
    public Object addMovieComment(@RequestBody MovieCommentRequest movieCommentRequest) {
        Integer commentId = movieCommentService.addMovieComment(movieCommentRequest);
        log.info(PREFIX_MOVIE_COMMENT + "Add Episode Comment successfully");
        return ApiResponeUtil.ResponseCreatedSuccess(commentId);
    }

    @PatchMapping("/{id}")
    public Object updateMovieComment(@PathVariable Integer id,@RequestBody MovieCommentRequest movieCommentRequest) {
        movieCommentService.updateMovieComment(id, movieCommentRequest);
        String massage="Updated episode comment";
        log.info(PREFIX_MOVIE_COMMENT + "Updated Episode Comment successfully");
        return ApiResponeUtil.ResponseOK(massage);
    }

    @DeleteMapping("/{ids}")
    public Object deletarMovieComment(@PathVariable List<Integer> ids) {
        movieCommentService.deleteMovieComment(ids);
        String massage="Deleted episode comment";
        log.info(PREFIX_MOVIE_COMMENT + "Deleted Episode Comment successfully");
        return ApiResponeUtil.ResponseOK(massage);
    }

    @GetMapping("/{movieid}")
    public Object getMovieComment(@PathVariable Integer movieid) {
        List<MovieCommentResponse> movieComments= movieCommentService.getMovieComment(movieid);
        log.info(PREFIX_MOVIE_COMMENT + "Get all Episode Comment by movieId successfully");
        return ApiResponeUtil.ResponseOK(movieComments);
    }
}
