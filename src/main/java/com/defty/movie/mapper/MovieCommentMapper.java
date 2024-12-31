package com.defty.movie.mapper;

import com.defty.movie.dto.request.MovieCommentRequest;
import com.defty.movie.dto.response.MovieCommentResponse;
import com.defty.movie.entity.MovieComment;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MovieCommentMapper {
    private final ModelMapper modelMapper;

    public MovieComment toMovieComment(MovieCommentRequest movieCommentRequest) {
        return modelMapper.map(movieCommentRequest, MovieComment.class);
    }

    public MovieCommentResponse toMovieCommentReSponse(MovieComment movieComment) {
        return modelMapper.map(movieComment, MovieCommentResponse.class);
    }
}
