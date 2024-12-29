package com.defty.movie.service.impl;

import com.defty.movie.dto.request.MovieCommentRequest;
import com.defty.movie.dto.response.ArticleCommentResponse;
import com.defty.movie.dto.response.MovieCommentResponse;
import com.defty.movie.entity.*;
import com.defty.movie.exception.NotFoundException;
import com.defty.movie.mapper.MovieCommentMapper;
import com.defty.movie.repository.IEpisodeRepository;
import com.defty.movie.repository.IMovieCommentRepository;
import com.defty.movie.service.IAuthUserService;
import com.defty.movie.service.IMovieCommentService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MovieCommentService implements IMovieCommentService {

    IMovieCommentRepository movieCommentRepository;
    IAuthUserService authUserService;
    IEpisodeRepository episodeRepository;
    MovieCommentMapper movieCommentMapper;

    @Override
    public Integer addMovieComment(MovieCommentRequest movieCommentRequest) {
        MovieComment movieComment = new MovieComment();
        Optional<User> user = authUserService.getCurrentUser();
        if(user.isPresent()) {
            movieComment.setUser(user.get());
        }else throw new NotFoundException("User not found");

        Optional<Episode> episode = episodeRepository.findById(movieCommentRequest.getEpisodeId());
        if(episode.isPresent()) {
            movieComment.setEpisode(episode.get());
        }else throw new NotFoundException("Episode not found");

        if(movieCommentRequest.getParentEpisodeCommentId()!=null) {
            Optional<MovieComment> movieCommentParent= movieCommentRepository.findById(movieCommentRequest.getParentEpisodeCommentId());
            if(movieCommentParent.isPresent()) {
                movieComment.setParentMovieComment(movieCommentParent.get());
            }else throw new NotFoundException("Parent episode comment not found");
        }

        movieComment.setStatus(1);

        movieComment.setContent(movieCommentRequest.getContent());

        MovieComment result=movieCommentRepository.save(movieComment);

        return result.getId();
    }

    @Override
    public void updateMovieComment(Integer id, MovieCommentRequest movieCommentRequest) {
        Optional<MovieComment> movieComment = movieCommentRepository.findById(id);
        if(movieComment.isPresent()) {
            MovieComment movieCommentUpdate = movieComment.get();
            BeanUtils.copyProperties(movieCommentRequest,movieCommentUpdate);
            movieCommentRepository.save(movieCommentUpdate);
        }else throw new NotFoundException("Movie Comment not found");
    }

    @Override
    public void deleteMovieComment(List<Integer> ids) {
        List<MovieComment> movieComments = movieCommentRepository.findAllById(ids);
        if(movieComments.size()!=ids.size()) throw new NotFoundException("Some Movie Comment not found");
        else {
            // xoa "cmt" bao gom ca "cmt con"
            for(MovieComment movieComment:movieComments) {
                if(movieComment.getParentMovieComment()==null) { // check xem cmt nay la cha hay con
                    Optional<List<MovieComment>> movieCommentSub= movieCommentRepository.findByParentMovieComment_Id(movieComment.getId());
                    if(movieCommentSub.isPresent() && movieCommentSub.get().size()>0) {
                        List<MovieComment> movieCommentSubUpdates = movieCommentSub.get();
                        for(MovieComment movieCommentUpdate:movieCommentSubUpdates) {
                            movieCommentUpdate.setStatus(0);
                        }
                        movieCommentRepository.saveAll(movieCommentSubUpdates);
                    }
                }
                movieComment.setStatus(0);
            }
            movieCommentRepository.saveAll(movieComments);
        }
    }

    @Override
    public List<MovieCommentResponse> getMovieComment(Integer movieId) {
        Optional<List<MovieComment>> movieComments = movieCommentRepository.findByEpisode_IdAndStatus(movieId, 1);
        if(movieComments.isPresent() && movieComments.get().size()>0) {
            List<MovieComment> movieCommentList = movieComments.get();
            List<MovieCommentResponse> movieCommentResponses = new ArrayList<>();
            for(MovieComment movieComment:movieCommentList) {
                movieCommentResponses.add(movieCommentMapper.toMovieCommentReSponse(movieComment));
            }
            return movieCommentResponses;
        }throw new NotFoundException("Episode Comment not found");
    }
}
