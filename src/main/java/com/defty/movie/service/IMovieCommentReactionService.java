package com.defty.movie.service;

import com.defty.movie.dto.request.MovieCommentReactionRequest;
import com.defty.movie.dto.request.MovieCommentReactionUpdateRequest;

public interface IMovieCommentReactionService {
    Integer addMovieCommentReaction(MovieCommentReactionRequest movieCommentReactionRequest);
    void updateMovieCommentReaction(MovieCommentReactionUpdateRequest movieCommentReactionUpdateRequest);
    void deleteMovieCommentReaction(Integer movieCommentReactionId);
}
