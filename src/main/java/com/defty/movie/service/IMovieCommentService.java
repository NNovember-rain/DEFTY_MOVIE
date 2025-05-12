package com.defty.movie.service;

import com.defty.movie.dto.request.MovieCommentRequest;
import com.defty.movie.dto.request.MovieCommentUpdateRequest;
import com.defty.movie.dto.response.MovieCommentResponse;
import com.defty.movie.dto.response.PageableResponse;
import com.defty.movie.entity.MovieComment;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IMovieCommentService {
    Integer addMovieComment(MovieCommentRequest movieCommentRequest);
    void updateMovieComment(Integer id, MovieCommentUpdateRequest movieCommentUpdateRequest);
    void deleteMovieComment(Integer id);
    PageableResponse<MovieCommentResponse> getMovieComment(Integer movieId, Pageable pageable);
    MovieComment getMovieCommentById(Integer id);
    List<MovieCommentResponse> getMovieCommentReplies(Integer parentCommentId, Pageable pageable);
}
