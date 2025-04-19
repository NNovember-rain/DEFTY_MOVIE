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
    private final ModelMapper modelMapper;
    private final IMovieCommentReactionRepository movieCommentReactionRepository;
    private final UserMapper userMapper;
    private final IMovieCommentRepository movieCommentRepository;

    public MovieComment toMovieComment(MovieCommentRequest movieCommentRequest) {
        return modelMapper.map(movieCommentRequest, MovieComment.class);
    }

    public MovieCommentResponse toMovieCommentResponse(MovieComment movieComment) {
        MovieCommentResponse movieCommentResponse = mapperMovieCommentResponse(movieComment);
        // Lấy tất cả replies từ các cấp con và gộp vào danh sách replies
        List<MovieCommentResponse> allReplies = new ArrayList<>();
        collectAllReplies(movieComment.getId(), allReplies);
        movieCommentResponse.setReplies(allReplies);
        return movieCommentResponse;
    }

    // Phương thức đệ quy để thu thập tất cả replies từ các cấp con
    private void collectAllReplies(Integer parentCommentId, List<MovieCommentResponse> allReplies) {
        Optional<List<MovieComment>> movieCommentsOptional = movieCommentRepository.findByParentMovieCommentIdAndStatus(parentCommentId, 1);
        if (movieCommentsOptional.isPresent()) {
            List<MovieComment> movieComments = movieCommentsOptional.get();
            for (MovieComment reply : movieComments) {
                // Chuyển đổi reply thành MovieCommentResponse
                MovieCommentResponse replyResponse = mapperMovieCommentResponse(reply);
                replyResponse.setReplyFrom(reply.getParentMovieComment().getUser().getUsername());
                allReplies.add(replyResponse);
                // Đệ quy để thu thập các replies của reply hiện tại
                collectAllReplies(reply.getId(), allReplies);
            }
        }
    }

    public MovieCommentResponse mapperMovieCommentResponse(MovieComment movieComment) {
        List<MovieCommentReaction> movieCommentReactions = movieCommentReactionRepository.findByMovieCommentId(movieComment.getId());
        MovieCommentResponse movieCommentResponse = new MovieCommentResponse();
        movieCommentResponse.setId(movieComment.getId());
        movieCommentResponse.setContent(movieComment.getContent());
        List<CommentReactionResponse> commentReactionResponses = new ArrayList<>();
        for (MovieCommentReaction movieCommentReaction : movieCommentReactions) {
            commentReactionResponses.add(toCommentReactionResponse(movieCommentReaction));
        }
        movieCommentResponse.setCreatedAt(movieComment.getCreatedDate());
        movieCommentResponse.setReactions(commentReactionResponses);
        EpisodeCommentUserResponse episodeCommentUserResponse = userMapper.toEpisodeCommentUserResponse(movieComment.getUser());
        movieCommentResponse.setUser(episodeCommentUserResponse);
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