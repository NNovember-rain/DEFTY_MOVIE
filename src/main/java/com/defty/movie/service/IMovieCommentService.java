package com.defty.movie.service;

import com.defty.movie.dto.request.ArticleCommentRequest;
import com.defty.movie.dto.request.MovieCommentRequest;
import com.defty.movie.dto.response.ArticleCommentResponse;
import com.defty.movie.dto.response.MovieCommentResponse;
import com.defty.movie.entity.MovieComment;

import java.util.List;

public interface IMovieCommentService {
    Integer addMovieComment(MovieCommentRequest movieCommentRequest);
    void updateMovieComment(Integer id, MovieCommentRequest movieCommentRequest);
    void deleteMovieComment(List<Integer> ids);
    List<MovieCommentResponse> getMovieComment(Integer movieId);
    MovieComment getMovieCommentById(Integer id);
}
