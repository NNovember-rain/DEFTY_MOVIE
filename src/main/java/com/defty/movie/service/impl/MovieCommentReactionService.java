package com.defty.movie.service.impl;

import com.defty.movie.dto.request.MovieCommentReactionRequest;
import com.defty.movie.dto.request.MovieCommentReactionUpdateRequest;
import com.defty.movie.entity.MovieCommentReaction;
import com.defty.movie.entity.MovieComment;
import com.defty.movie.entity.User;
import com.defty.movie.exception.NotFoundException;
import com.defty.movie.repository.IMovieCommentReactionRepository;
import com.defty.movie.service.IMovieCommentService;
import com.defty.movie.service.IAuthUserService;
import com.defty.movie.service.IMovieCommentReactionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MovieCommentReactionService implements IMovieCommentReactionService {

    private final IAuthUserService authUserService;
    private final IMovieCommentService movieCommentService;
    private final IMovieCommentReactionRepository movieCommentReactionRepository;

    @Override
    public Integer addMovieCommentReaction(MovieCommentReactionRequest movieCommentReactionRequest) {
        MovieCommentReaction movieCommentReaction = new MovieCommentReaction();
        Optional<User> user = authUserService.getCurrentUser();
        if(user.isPresent()) {
            movieCommentReaction.setUser(user.get());
        }else{
            throw new NotFoundException("User not found");
        }

        MovieComment movieComment=movieCommentService.getMovieCommentById(movieCommentReactionRequest.getMovieCommentId());

        movieCommentReaction.setMovieComment(movieComment);

        movieCommentReaction.setContent(movieCommentReactionRequest.getContent());

        movieCommentReaction.setUser(user.get());

        movieCommentReaction.setStatus(1);

        MovieCommentReaction movieCommentReactionSaved=movieCommentReactionRepository.save(movieCommentReaction);

        return movieCommentReactionSaved.getId();
    }

    @Override
    public void updateMovieCommentReaction(MovieCommentReactionUpdateRequest movieCommentReactionUpdateRequest) {
        MovieCommentReaction movieCommentReaction = movieCommentReactionRepository.findById(movieCommentReactionUpdateRequest.getMovieCommentReactionId())
                .orElseThrow(() -> new NotFoundException("MovieCommentReaction not found"));
        movieCommentReaction.setContent(movieCommentReactionUpdateRequest.getContent());
        movieCommentReactionRepository.save(movieCommentReaction);
    }

    @Override
    public void deleteMovieCommentReaction(Integer movieCommentReactionId) {
        MovieCommentReaction movieCommentReaction = movieCommentReactionRepository.findById(movieCommentReactionId)
                .orElseThrow(() -> new NotFoundException("MovieCommentReaction with id not found"));
        movieCommentReaction.setStatus(0);
        movieCommentReactionRepository.save(movieCommentReaction);
    }
}
