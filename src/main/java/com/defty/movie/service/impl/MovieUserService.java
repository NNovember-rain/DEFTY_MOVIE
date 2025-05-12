package com.defty.movie.service.impl;

import com.defty.movie.dto.response.*;
import com.defty.movie.entity.*;
import com.defty.movie.exception.NotFoundException;
import com.defty.movie.repository.IEpisodeRepository;
import com.defty.movie.repository.IMovieRepository;
import com.defty.movie.service.IMovieUserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MovieUserService implements IMovieUserService {

    IMovieRepository movieRepository;
    IEpisodeRepository episodeRepository;


    @Override
    public List<MovieSearchResultResponse> getMovies(String title) {
        List<Movie> movies =movieRepository.findByTitleContainingIgnoreCaseAndStatus(title,1);
        List<MovieSearchResultResponse> results = new ArrayList<>();
        for (Movie movie : movies) {
            MovieSearchResultResponse movieSearchResultResponse = new MovieSearchResultResponse();
            BeanUtils.copyProperties(movie,movieSearchResultResponse);

            Set<Episode> episodes=movie.getEpisodes();
            List<EpisodeNameResponse> episodeNameResponses = new ArrayList<>();
            for(Episode episode : episodes){
                EpisodeNameResponse episodeNameResponse = new EpisodeNameResponse();
                episodeNameResponse.setNumber(episode.getNumber());
                episodeNameResponse.setSlug(episode.getSlug());
                episodeNameResponses.add(episodeNameResponse);
            }

            List<Actor> actors=movie.getActors();
            List<ActorNameResponse> actorNameResponses = new ArrayList<>();
            for(Actor actor : actors){
                ActorNameResponse actorNameResponse = new ActorNameResponse();
                actorNameResponse.setName(actor.getFullName());
                actorNameResponse.setSlug(actor.getSlug());
                actorNameResponses.add(actorNameResponse);
            }

            Director director = movie.getDirector();
            DirectorNameResponse directorNameResponse = new DirectorNameResponse();
            directorNameResponse.setName(director.getFullName());
            directorNameResponse.setSlug(director.getSlug());

            movieSearchResultResponse.setDirector(directorNameResponse);
            movieSearchResultResponse.setActors(actorNameResponses);
            movieSearchResultResponse.setEpisodes(episodeNameResponses);
            results.add(movieSearchResultResponse);
        }
        return results;
    }

    @Override
    public List<MovieNameResponse> getAllMovie(String title) {
        Pageable pageable = PageRequest.of(0, 9);
        List<Movie> movies= movieRepository.findByStatusAndTitleContainingIgnoreCase(1,title, pageable);
        List<MovieNameResponse> movieNameResponses = new ArrayList<>();
        for (Movie movie : movies) {
            MovieNameResponse movieNameResponse = new MovieNameResponse();
            movieNameResponse.setName(movie.getTitle());
            movieNameResponse.setSlug(movie.getSlug());
            movieNameResponses.add(movieNameResponse);
        }
        return movieNameResponses;
    }

    @Override
    public MovieDetailResponse getMovieDetails(String slugMovie) {
        Optional<Movie> movieOptional = movieRepository.findBySlugAndStatus(slugMovie,1);
        if(movieOptional.isPresent()){

            Movie movie = movieOptional.get();
            MovieDetailResponse movieDetailResponse= new MovieDetailResponse();
            Episode firstEpisode = episodeRepository.findByMovieIdAndNumberAndStatus(movie.getId(),1,1);

            Set<MovieCategory> movieCategories = movie.getMovieCategories();
            List<CategoryNameResponse> categoryNames = new ArrayList<>();
            for (MovieCategory movieCategory : movieCategories) {
                Category category = movieCategory.getCategory();
                if(category.getStatus()==1) {
                    CategoryNameResponse categoryResponse = new CategoryNameResponse();
                    categoryResponse.setName(category.getName());
                    categoryResponse.setSlug(category.getSlug());
                    categoryNames.add(categoryResponse);
                }
            }

            Director director = movie.getDirector();
            MovieNameResponse directorResponse = new MovieNameResponse();
            if(director.getStatus()==1) {
                directorResponse.setName(director.getFullName());
                directorResponse.setSlug(director.getSlug());
                directorResponse.setThumbnail(director.getAvatar());
            }

            List<ActorNameResponse> actorNames = new ArrayList<>();
            List<Actor> actors = movie.getActors();
            for (Actor actor : actors) {
                if(actor.getStatus()==1) {
                    ActorNameResponse actorResponse = new ActorNameResponse();
                    actorResponse.setName(actor.getFullName());
                    actorResponse.setSlug(actor.getSlug());
                    actorResponse.setAvatar(actor.getAvatar());
                    actorNames.add(actorResponse);
                }
            }

            Set<Episode> episodes = movie.getEpisodes();

            movieDetailResponse.setId(movie.getId());
            movieDetailResponse.setTitle(movie.getTitle());
            movieDetailResponse.setCategory(categoryNames);
            movieDetailResponse.setDirector(directorResponse);
            movieDetailResponse.setActor(actorNames);
            movieDetailResponse.setSlug(movie.getSlug());
            movieDetailResponse.setDescription(movie.getDescription());
            movieDetailResponse.setReleaseDate(movie.getReleaseDate());
            movieDetailResponse.setCoverImage(movie.getCoverImage());
            movieDetailResponse.setDuration(episodes.size());
            movieDetailResponse.setTrailer(movie.getTrailer());
            movieDetailResponse.setFirstEpisodeSlug(firstEpisode.getSlug());

            return movieDetailResponse;
        }else throw new NotFoundException("Movie not found");
    }

    @Override
    public PageableResponse<EpisodeResponse> getEpisodes(String slugMovie, Pageable pageable) {
        Optional<Movie> movieOptional = movieRepository.findBySlugAndStatus(slugMovie,1);
        if(movieOptional.isPresent()){
            Movie movie = movieOptional.get();
            PageableResponse<EpisodeResponse> episodePageable = new PageableResponse<>();
            Long totalElement= (long) episodeRepository.findByMovieIdAndStatus(movie.getId(),1).size();
            List<Episode> episodes = episodeRepository.findByMovieIdAndStatusOrderByNumber(movie.getId(),1,pageable).getContent();
            List<EpisodeResponse> episodeResponses = new ArrayList<>();
            for (Episode episode : episodes) {
                EpisodeResponse episodeResponse = new EpisodeResponse();
                BeanUtils.copyProperties(episode, episodeResponse);
                episodeResponses.add(episodeResponse);
            }
            episodePageable.setContent(episodeResponses);
            episodePageable.setTotalElements(totalElement);
            return episodePageable;
        }else throw new NotFoundException("Movie not found");
    }

    @Override
    public MovieDetailDirectorActorResponse getMovieDetailActor(String slugMovie) {
        Optional<Movie> movieOptional = movieRepository.findBySlugAndStatus(slugMovie,1);
        if(movieOptional.isPresent()){
            Movie movie = movieOptional.get();
            MovieDetailDirectorActorResponse movieDetailDirectorActorResponse = new MovieDetailDirectorActorResponse();
            List<Actor> actors=movie.getActors();
            Director director=movie.getDirector();

            List<ActorMovieDetailResponse> actorResponses=new ArrayList<>();
            for(Actor actor:actors){
                if(actor.getStatus()==1) {
                    ActorMovieDetailResponse actorResponse = new ActorMovieDetailResponse();
                    actorResponse.setFullName(actor.getFullName());
                    actorResponse.setAvatar(actor.getAvatar());
                    actorResponse.setSlug(actor.getSlug());
                    List<Movie> movies= movieRepository.findTop2NewestMoviesByActorId(actor.getId());
                    List<MovieNameResponse> movieResponses=new ArrayList<>();
                    for(Movie movie1:movies){
                        if(movie1.getStatus()==1) {
                            MovieNameResponse movieResponse = new MovieNameResponse();
                            movieResponse.setName(movie1.getTitle());
                            movieResponse.setSlug(movie1.getSlug());
                            movieResponse.setThumbnail(movie1.getThumbnail());
                            movieResponses.add(movieResponse);
                        }
                    }
                    actorResponse.setMovies(movieResponses);
                    actorResponses.add(actorResponse);
                }
            }

            List<Movie> movies=movieRepository.findTop2NewestMoviesByDirectorId(director.getId());
            List<MovieNameResponse> movieResponses=new ArrayList<>();
            for(Movie movie1:movies){
                if(movie1.getStatus()==1) {
                    MovieNameResponse movieResponse = new MovieNameResponse();
                    movieResponse.setName(movie1.getTitle());
                    movieResponse.setSlug(movie1.getSlug());
                    movieResponse.setThumbnail(movie1.getThumbnail());
                    movieResponses.add(movieResponse);
                }

            }
            DirectorMovieDetailResponse directorResponse=new DirectorMovieDetailResponse();
            directorResponse.setFullName(director.getFullName());
            directorResponse.setAvatar(director.getAvatar());
            directorResponse.setSlug(director.getSlug());
            directorResponse.setMovies(movieResponses);

            movieDetailDirectorActorResponse.setDirectorNameResponse(directorResponse);
            movieDetailDirectorActorResponse.setActors(actorResponses);
            return movieDetailDirectorActorResponse;
        }else throw new NotFoundException("Movie not found");
    }

    @Override
    public Movie getMovieBySlugEpisode(String slug) {
        Optional<Episode> episode = episodeRepository.findBySlugAndStatus(slug, 1);
        if(episode.isPresent()){
            Movie movie = episode.get().getMovie();
            return movie;
        }else {
            log.error("Episode not found with slug: {}", slug);
            throw new NotFoundException("The episode doesn't exist with slug Movie");
        }
    }

    @Override
    public List<MovieNameResponse> getAllMovies() {
        Pageable pageable = PageRequest.of(0, 10);
        List<Movie> movies=movieRepository.findAllByStatus(pageable,1);
        List<MovieNameResponse> movieResponses=new ArrayList<>();
        for (Movie movie : movies) {
            MovieNameResponse movieResponse = new MovieNameResponse();
            movieResponse.setThumbnail(movie.getThumbnail());
            movieResponse.setName(movie.getTitle());
            movieResponse.setSlug(movie.getSlug());
            movieResponses.add(movieResponse);
        }
        return movieResponses;
    }

    @Override
    public List<MovieAppSearchResultResponse> getMoviesAppResult(String title) {
        List<Movie> movies =movieRepository.findByTitleContainingIgnoreCaseAndStatus(title,1);
        List<MovieAppSearchResultResponse> movieAppSearchResultResponses=new ArrayList<>();
        for (Movie movie : movies) {
            MovieAppSearchResultResponse movieAppSearchResultResponse = new MovieAppSearchResultResponse();
            BeanUtils.copyProperties(movie,movieAppSearchResultResponse);

            List<Actor> actors=movie.getActors();
            List<String> actorNameResponses = new ArrayList<>();
            for(Actor actor : actors){
                actorNameResponses.add(actor.getFullName());
            }
            movieAppSearchResultResponse.setActors(actorNameResponses);
            Set<MovieCategory> movieCategories=movie.getMovieCategories();
            List<String> categorys=new ArrayList<>();
            for(MovieCategory movieCategory : movieCategories){
                Category category = movieCategory.getCategory();
                categorys.add(category.getName());
            }
            movieAppSearchResultResponse.setCategories(categorys);
            movieAppSearchResultResponses.add(movieAppSearchResultResponse);
        }
        return movieAppSearchResultResponses;
    }
}



