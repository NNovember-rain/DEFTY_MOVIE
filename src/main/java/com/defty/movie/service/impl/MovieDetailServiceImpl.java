package com.defty.movie.service.impl;

import com.defty.movie.dto.response.*;
import com.defty.movie.entity.*;
import com.defty.movie.exception.NotFoundException;
import com.defty.movie.repository.IActorRepository;
import com.defty.movie.repository.IDirectorRepository;
import com.defty.movie.repository.IMovieRepository;
import com.defty.movie.service.IMovieDetailService;
import com.fasterxml.jackson.databind.util.BeanUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MovieDetailServiceImpl implements IMovieDetailService {

    IMovieRepository movieRepository;
    IActorRepository actorRepository;
    IDirectorRepository directorRepository;


    @Override
    public MovieDetailResponse getMovieDetails(String slugMovie) {
        Optional<Movie> movieOptional = movieRepository.findBySlug((slugMovie));
        if(movieOptional.isPresent()){
            Movie movie = movieOptional.get();
            MovieDetailResponse movieDetailResponse= new MovieDetailResponse();

            Set<MovieCategory> movieCategories = movie.getMovieCategories();
            List<String> categoryNames = new ArrayList<>();
            for (MovieCategory movieCategory : movieCategories) {
                categoryNames.add(movieCategory.getCategory().getName());
            }

            String directorName = movie.getDirector().getFullName();

            List<String> actorNames = new ArrayList<>();
            Set<Actor> actors = movie.getActors();
            for (Actor actor : actors) {
                actorNames.add(actor.getFullName());
            }

            Set<Episode> episodes = movie.getEpisodes();

            movieDetailResponse.setTitle(movie.getTitle());
            movieDetailResponse.setCategory(categoryNames);
            movieDetailResponse.setDirector(directorName);
            movieDetailResponse.setActor(actorNames);
            movieDetailResponse.setDescription(movie.getDescription());
            movieDetailResponse.setReleaseDate(movie.getReleaseDate());
            movieDetailResponse.setCoverImage(movie.getCoverImage());
            movieDetailResponse.setDuration(episodes.size());
            movieDetailResponse.setTrailer(movie.getTrailer());

            return movieDetailResponse;
        }else throw new NotFoundException("Movie not found");
    }

    @Override
    public List<EpisodeResponse> getEpisodes(String slugMovie) {
        Optional<Movie> movieOptional = movieRepository.findBySlug((slugMovie));
        if(movieOptional.isPresent()){
            Movie movie = movieOptional.get();
            Set<Episode> episodes = movie.getEpisodes();
            List<EpisodeResponse> episodeResponses = new ArrayList<>();
            for (Episode episode : episodes) {
                EpisodeResponse episodeResponse = new EpisodeResponse();
                BeanUtils.copyProperties(episode, episodeResponse);
                episodeResponses.add(episodeResponse);
            }
            return episodeResponses;
        }else throw new NotFoundException("Movie not found");
    }

    @Override
    public MovieDetailActorResponse getMovieDetailActor(String slugMovie) {
        Optional<Movie> movieOptional = movieRepository.findBySlug((slugMovie));
        if(movieOptional.isPresent()){
            Movie movie = movieOptional.get();
            MovieDetailActorResponse movieDetailActorResponse = new MovieDetailActorResponse();
            Set<Actor> actors=movie.getActors();
            Director director=movie.getDirector();

            List<ActorResponse> actorResponses=new ArrayList<>();
            for(Actor actor:actors){
                ActorResponse actorResponse = new ActorResponse();
                BeanUtils.copyProperties(actor, actorResponse);
                List<Movie> movies= movieRepository.findTop2NewestMoviesByActorId(actor.getId());
                List<MovieResponse> movieResponses=new ArrayList<>();
                for(Movie movie1:movies){
                    MovieResponse movieResponse = new MovieResponse();
                    BeanUtils.copyProperties(movie1, movieResponse);
                    movieResponses.add(movieResponse);
                }
                actorResponse.setMovies(movieResponses);
                actorResponses.add(actorResponse);
            }

            List<Movie> movies=movieRepository.findTop2NewestMoviesByDirectorId(director.getId());
            List<MovieResponse> movieResponses=new ArrayList<>();
            for(Movie movie1:movies){
                MovieResponse movieResponse = new MovieResponse();
                BeanUtils.copyProperties(movie1, movieResponse);
                movieResponses.add(movieResponse);
            }
            DirectorResponse directorResponse=new DirectorResponse();
            BeanUtils.copyProperties(director, directorResponse);
            directorResponse.setMovies(movieResponses);

            movieDetailActorResponse.setDirectorResponse(directorResponse);
            movieDetailActorResponse.setActors(actorResponses);
            return movieDetailActorResponse;
        }else throw new NotFoundException("Movie not found");
    }
}
