package com.defty.movie.service.impl;

import com.defty.movie.dto.response.*;
import com.defty.movie.entity.*;
import com.defty.movie.exception.NotFoundException;
import com.defty.movie.repository.IEpisodeRepository;
import com.defty.movie.repository.IMovieRepository;
import com.defty.movie.service.IMovieDetailService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
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
public class MovieDetailServiceImpl implements IMovieDetailService {

    IMovieRepository movieRepository;
    IEpisodeRepository episodeRepository;

    @Override
    public MovieDetailResponse getMovieDetails(String slugMovie) {
        Optional<Movie> movieOptional = movieRepository.findBySlugAndStatus(slugMovie,1);
        if(movieOptional.isPresent()){
            Movie movie = movieOptional.get();
            MovieDetailResponse movieDetailResponse= new MovieDetailResponse();

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
            }

            List<ActorNameResponse> actorNames = new ArrayList<>();
            Set<Actor> actors = movie.getActors();
            for (Actor actor : actors) {
                if(actor.getStatus()==1) {
                    ActorNameResponse actorResponse = new ActorNameResponse();
                    actorResponse.setName(actor.getFullName());
                    actorResponse.setSlug(actor.getSlug());
                    actorNames.add(actorResponse);
                }
            }

            Set<Episode> episodes = movie.getEpisodes();

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
            Set<Actor> actors=movie.getActors();
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
}
