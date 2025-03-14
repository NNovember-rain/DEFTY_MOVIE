package com.defty.movie.service.impl;

import com.defty.movie.dto.response.*;
import com.defty.movie.entity.*;
import com.defty.movie.exception.NotFoundException;
import com.defty.movie.repository.IActorRepository;
import com.defty.movie.repository.IDirectorRepository;
import com.defty.movie.repository.IMovieRepository;
import com.defty.movie.service.IMovieDetailService;
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
        Optional<Movie> movieOptional = movieRepository.findBySlugAndStatus(slugMovie,1);
        if(movieOptional.isPresent()){
            Movie movie = movieOptional.get();
            MovieDetailResponse movieDetailResponse= new MovieDetailResponse();

            Set<MovieCategory> movieCategories = movie.getMovieCategories();
            List<MovieDetailCategoryResponse> categoryNames = new ArrayList<>();
            for (MovieCategory movieCategory : movieCategories) {
                Category category = movieCategory.getCategory();
                MovieDetailCategoryResponse categoryResponse = new MovieDetailCategoryResponse();
                categoryResponse.setName(category.getName());
                categoryResponse.setSlug(category.getSlug());
                categoryNames.add(categoryResponse);
            }

            Director director = movie.getDirector();
            MovieDetailDirectorResponse directorResponse = new MovieDetailDirectorResponse();
            directorResponse.setName(director.getFullName());
            directorResponse.setSlug(director.getSlug());

            List<MovieDetailActorResponse> actorNames = new ArrayList<>();
            Set<Actor> actors = movie.getActors();
            for (Actor actor : actors) {
                MovieDetailActorResponse actorResponse = new MovieDetailActorResponse();
                actorResponse.setName(actor.getFullName());
                actorResponse.setSlug(actor.getSlug());
                actorNames.add(actorResponse);
            }

            Set<Episode> episodes = movie.getEpisodes();

            movieDetailResponse.setTitle(movie.getTitle());
            movieDetailResponse.setCategory(categoryNames);
            movieDetailResponse.setDirector(directorResponse);
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
        Optional<Movie> movieOptional = movieRepository.findBySlugAndStatus(slugMovie,1);
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
    public MovieDetailDirectorActorResponse getMovieDetailActor(String slugMovie) {
        Optional<Movie> movieOptional = movieRepository.findBySlugAndStatus(slugMovie,1);
        if(movieOptional.isPresent()){
            Movie movie = movieOptional.get();
            MovieDetailDirectorActorResponse movieDetailDirectorActorResponse = new MovieDetailDirectorActorResponse();
            Set<Actor> actors=movie.getActors();
            Director director=movie.getDirector();

            List<ActorMovieDetailResponse> actorResponses=new ArrayList<>();
            for(Actor actor:actors){
                ActorMovieDetailResponse actorResponse = new ActorMovieDetailResponse();
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
            DirectorMovieDetailResponse directorResponse=new DirectorMovieDetailResponse();
            BeanUtils.copyProperties(director, directorResponse);
            directorResponse.setMovies(movieResponses);

            movieDetailDirectorActorResponse.setDirectorResponse(directorResponse);
            movieDetailDirectorActorResponse.setActors(actorResponses);
            return movieDetailDirectorActorResponse;
        }else throw new NotFoundException("Movie not found");
    }
}
