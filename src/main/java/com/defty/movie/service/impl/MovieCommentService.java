package com.defty.movie.service.impl;

import com.defty.movie.dto.request.MovieCommentRequest;
import com.defty.movie.dto.request.MovieCommentUpdateRequest;
import com.defty.movie.dto.response.ArticleResponse;
import com.defty.movie.dto.response.MovieCommentResponse;
import com.defty.movie.dto.response.PageableResponse;
import com.defty.movie.exception.FieldRequiredException;
import com.defty.movie.exception.NotFoundException;
import com.defty.movie.mapper.MovieCommentMapper;
import com.defty.movie.entity.Episode;
import com.defty.movie.entity.MovieComment;
import com.defty.movie.entity.User;
import com.defty.movie.repository.IEpisodeRepository;
import com.defty.movie.repository.IMovieCommentRepository;
import com.defty.movie.service.IAuthUserService;
import com.defty.movie.service.IMovieCommentService;
import com.defty.movie.utils.ApiResponeUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MovieCommentService implements IMovieCommentService {

    IMovieCommentRepository movieCommentRepository;
    IAuthUserService authUserService;
    IEpisodeRepository episodeRepository;
    MovieCommentMapper movieCommentMapper;

    String PREFIX_MOVIE_COMMENT = "MOVIE_COMMENT | ";

    @Override
    public Integer addMovieComment(MovieCommentRequest movieCommentRequest) {
        MovieComment movieComment = new MovieComment();
        Optional<User> user = authUserService.getCurrentUser();
        if(user.isPresent()) {
            log.info(PREFIX_MOVIE_COMMENT + "Get current user success");
            movieComment.setUser(user.get());
        }else {
            log.error("{}User not found", PREFIX_MOVIE_COMMENT);
            throw new NotFoundException("User not found");
        }

        Optional<Episode> episode = episodeRepository.findById(movieCommentRequest.getEpisodeId());
        if(episode.isPresent()) {
            log.info(PREFIX_MOVIE_COMMENT + "Get Episode by episodeId="+movieCommentRequest.getEpisodeId()+ " success");
            movieComment.setEpisode(episode.get());
        }else {
            log.error("{}Article not found", PREFIX_MOVIE_COMMENT);
            throw new NotFoundException("Episode not found");
        }

        if(movieCommentRequest.getContent() == null || movieCommentRequest.getContent().isEmpty()) throw new FieldRequiredException("Field ïs required");

        if(movieCommentRequest.getParentId()!=null) {
            Optional<MovieComment> movieCommentParent= movieCommentRepository.findByIdAndStatus(movieCommentRequest.getParentId(),1);
            if(movieCommentParent.isPresent()) {
                log.info(PREFIX_MOVIE_COMMENT + "Get EpisodeCommentParent by episodeCommentParentId="+movieCommentRequest.getParentId()+ " success");
                movieComment.setParentMovieComment(movieCommentParent.get());
            }else {
                log.error("{}Parent episode comment not found", PREFIX_MOVIE_COMMENT);
                throw new NotFoundException("Parent episode comment not found");
            }
        }

        movieComment.setStatus(1);

        movieComment.setContent(movieCommentRequest.getContent());

        MovieComment result=movieCommentRepository.save(movieComment);

        return result.getId();
    }

    @Override
    public void updateMovieComment(Integer id, MovieCommentUpdateRequest movieCommentUpdateRequest) {
        if (movieCommentUpdateRequest.getContent() == null || movieCommentUpdateRequest.getContent().isEmpty())
            throw new FieldRequiredException("Field ïs required");

        Optional<MovieComment> movieCommentOptional = movieCommentRepository.findById(id);
        if(movieCommentOptional.isPresent()) {
            MovieComment movieComment = movieCommentOptional.get();
            movieComment.setContent(movieCommentUpdateRequest.getContent());
            movieCommentRepository.save(movieComment);
        }else {
            log.error("{}Movie Comment not found", PREFIX_MOVIE_COMMENT);
            throw new NotFoundException("Movie Comment not found");
        }
    }

    @Override
    public void deleteMovieComment(Integer id) {
        Optional<MovieComment> movieCommentOptional = movieCommentRepository.findById(id);
        if (movieCommentOptional.isPresent()) {
            MovieComment movieComment = movieCommentOptional.get();
            disableCommentAndChildren(movieComment);
        } else {
            log.error("{}Some Movie Comment not found", PREFIX_MOVIE_COMMENT);
            throw new NotFoundException("Some Movie Comment not found");
        }
    }


    @Override
    public PageableResponse<MovieCommentResponse> getMovieComment(Integer episodeId, Pageable pageable) {

        Page<MovieComment> movieCommentPage = movieCommentRepository.findByEpisodeIdAndParentMovieCommentIsNullAndStatus(episodeId, 1, pageable);
        if (!movieCommentPage.hasContent()) {
            throw new NotFoundException("No movie comment found");
        }
        int totalElements = movieCommentRepository.findAllByEpisodeIdAndStatus(episodeId,1).size();
        List<MovieCommentResponse> responseList = movieCommentPage.getContent().stream()
                .map(comment -> {
                    MovieCommentResponse response = movieCommentMapper.mapperMovieCommentResponse(comment);
                    long replyCount = movieCommentRepository.countByParentMovieCommentIdAndStatus(comment.getId(), 1);
                    response.setTotalReply((int) replyCount);
                    response.setReplyTo(null);
                    return response;
                })
                .collect(Collectors.toList());
        return PageableResponse.<MovieCommentResponse>builder()
                .content(responseList)
                .totalElements(totalElements+0L)
                .build();
    }

    @Override
    public MovieComment getMovieCommentById(Integer id) {
        return movieCommentRepository.findById(id).orElseThrow(() -> new NotFoundException("Movie Comment not found"));
    }

    @Override
    public List<MovieCommentResponse> getMovieCommentReplies(Integer parentCommentId, Pageable pageable) {
        // 1. (Tùy chọn nhưng nên có) Kiểm tra comment cha có tồn tại không
        MovieComment movieParentComment=movieCommentRepository.findByIdAndStatus(parentCommentId, 1)
                .orElseThrow(() -> {
                    return new NotFoundException("Parent comment not found with id: " + parentCommentId);
                });

        User user= movieParentComment.getUser();
        // 2. Sử dụng native query đã tạo trong Repository để lấy những thằng con
        Page<MovieComment> repliesPage = movieCommentRepository.findCommentDescendantsBreadthFirstPaginated(parentCommentId, 1, pageable);

        List<MovieComment> commentsInPage = repliesPage.getContent();
        if (commentsInPage.isEmpty()) {
            throw new NotFoundException("No movie comment found");
        }
        List<MovieCommentResponse> responseList = commentsInPage.stream().map(comment -> {
            // lấy cmt cha
            MovieComment parentComment= movieCommentRepository.findByIdAndStatus(comment.getParentMovieComment().getId(), 1).orElseThrow(() -> new NotFoundException("Parent comment not found"));

            MovieCommentResponse response = movieCommentMapper.mapperMovieCommentResponse(comment);
            long ownRepliesCount = movieCommentRepository.countByParentMovieCommentIdAndStatus(comment.getId(), 1);
            response.setTotalReply((int) ownRepliesCount);
            response.setReplyTo(parentComment.getUser().getFullName());
            return response;
        }).collect(Collectors.toList());

        return responseList; // Trả về danh sách comment của trang hiện tại
    }


    private void disableCommentAndChildren(MovieComment comment) {
        comment.setStatus(0);
        movieCommentRepository.save(comment);
        Optional<List<MovieComment>> subCommentsOpt = movieCommentRepository.findByParentMovieComment_Id(comment.getId());
        if (subCommentsOpt.isPresent() && !subCommentsOpt.get().isEmpty()) {
            for (MovieComment subComment : subCommentsOpt.get()) {
                disableCommentAndChildren(subComment);
            }
        }
    }

}
