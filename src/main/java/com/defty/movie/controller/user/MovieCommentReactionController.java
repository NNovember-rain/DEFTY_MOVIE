package com.defty.movie.controller.user;

import com.defty.movie.dto.request.MovieCommentReactionRequest;
import com.defty.movie.dto.request.MovieCommentReactionUpdateRequest;
import com.defty.movie.service.IMovieCommentReactionService;
import com.defty.movie.utils.ApiResponeUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/user/movie-comment-reaction")
public class MovieCommentReactionController {
    private final IMovieCommentReactionService movieCommentReactionService;

    @PostMapping()
    public Object addMovieCommentReaction(@RequestBody MovieCommentReactionRequest movieCommentReactionRequest) {
        Integer id= movieCommentReactionService.addMovieCommentReaction(movieCommentReactionRequest);
        return ApiResponeUtil.ResponseCreatedSuccess(id);
    }


    @PatchMapping()
    public Object updateMovieCommentReaction(@RequestBody MovieCommentReactionUpdateRequest movieCommentReactionUpdateRequest) {
        movieCommentReactionService.updateMovieCommentReaction(movieCommentReactionUpdateRequest);
        String message="Successfully updated movie comment reaction";
        return ApiResponeUtil.ResponseOK(message);
    }

    @DeleteMapping("/{id}")
    public Object deleteMovieCommentReaction(@PathVariable Integer id) {
        movieCommentReactionService.deleteMovieCommentReaction(id);
        String message="Successfully deleted movie comment reaction";
        return ApiResponeUtil.ResponseOK(message);
    }
}
