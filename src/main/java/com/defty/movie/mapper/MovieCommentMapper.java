package com.defty.movie.mapper;

import com.defty.movie.dto.request.MovieCommentRequest;
import com.defty.movie.dto.response.CommentReactionResponse;
import com.defty.movie.dto.response.MovieCommentResponse;
import com.defty.movie.entity.MovieComment;
import com.defty.movie.entity.MovieCommentReaction;
import com.defty.movie.repository.IMovieCommentReactionRepository;
import com.defty.movie.repository.IMovieCommentRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class MovieCommentMapper {
    private final ModelMapper modelMapper;
    private final IMovieCommentReactionRepository movieCommentReactionRepository;
    private final UserMapper userMapper;

    public MovieComment toMovieComment(MovieCommentRequest movieCommentRequest) {
        return modelMapper.map(movieCommentRequest, MovieComment.class);
    }

    public MovieCommentResponse toMovieCommentReSponse(MovieComment movieComment) {
        List<MovieCommentReaction> movieCommentReactions= movieCommentReactionRepository.findByMovieCommentId(movieComment.getId());
        MovieCommentResponse movieCommentResponse=modelMapper.map(movieComment, MovieCommentResponse.class);
        List<CommentReactionResponse> commentReactionResponses= new ArrayList<>();
        for(MovieCommentReaction movieCommentReaction:movieCommentReactions){
            CommentReactionResponse commentReactionResponse=new CommentReactionResponse();
            commentReactionResponse.setContent(movieCommentReaction.getContent());
            commentReactionResponse.setUserResponse(userMapper.toUserResponse(movieCommentReaction.getUser()));
            commentReactionResponses.add(commentReactionResponse);
        }
        movieCommentResponse.setCommentReactionRespons(commentReactionResponses);
        return movieCommentResponse;
    }
}
