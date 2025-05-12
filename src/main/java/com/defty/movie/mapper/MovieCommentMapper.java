package com.defty.movie.mapper;

import com.defty.movie.dto.request.MovieCommentRequest;
import com.defty.movie.dto.response.CommentReactionResponse;
import com.defty.movie.dto.response.EpisodeCommentUserResponse;
import com.defty.movie.dto.response.MovieCommentReplyResponse;
import com.defty.movie.dto.response.MovieCommentResponse;
import com.defty.movie.entity.MovieComment;
import com.defty.movie.entity.MovieCommentReaction;
import com.defty.movie.repository.IMovieCommentReactionRepository;
import com.defty.movie.repository.IMovieCommentRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class MovieCommentMapper {
    // ... các dependency khác ...
    private final IMovieCommentReactionRepository movieCommentReactionRepository;
    private final UserMapper userMapper;
    // private final IMovieCommentRepository movieCommentRepository; // Có thể không cần ở đây nữa nếu service xử lý hết

    // Phương thức map các trường cơ bản, user, reactions
    public MovieCommentResponse mapperMovieCommentResponse(MovieComment movieComment) {
        MovieCommentResponse movieCommentResponse = new MovieCommentResponse();
        movieCommentResponse.setId(movieComment.getId());
        movieCommentResponse.setContent(movieComment.getContent());
        movieCommentResponse.setCreatedAt(movieComment.getCreatedDate()); // Hoặc .getCreatedAt()
        if(movieComment.getParentMovieComment()!=null) {
            movieCommentResponse.setParentCommentId(movieComment.getParentMovieComment().getId());
        }

        EpisodeCommentUserResponse episodeCommentUserResponse = userMapper.toEpisodeCommentUserResponse(movieComment.getUser());
        movieCommentResponse.setUser(episodeCommentUserResponse);

        List<MovieCommentReaction> movieCommentReactions = movieCommentReactionRepository.findByMovieCommentId(movieComment.getId());
        List<CommentReactionResponse> commentReactionResponses = new ArrayList<>();
        for (MovieCommentReaction movieCommentReaction : movieCommentReactions) {
            commentReactionResponses.add(toCommentReactionResponse(movieCommentReaction));
        }
        movieCommentResponse.setReactions(commentReactionResponses);

        // replyFrom và replyCount sẽ được service set cụ thể cho từng API
        return movieCommentResponse;
    }

    public CommentReactionResponse toCommentReactionResponse(MovieCommentReaction movieCommentReaction) {
        CommentReactionResponse commentReactionResponse = new CommentReactionResponse();
        commentReactionResponse.setContent(movieCommentReaction.getContent());
        commentReactionResponse.setCreatedDate(movieCommentReaction.getCreatedDate());
        commentReactionResponse.setUser(userMapper.toEpisodeCommentUserResponse(movieCommentReaction.getUser()));
        return commentReactionResponse;
    }
}