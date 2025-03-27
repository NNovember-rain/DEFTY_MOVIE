package com.defty.movie.service.impl;

import com.defty.movie.dto.response.*;
import com.defty.movie.entity.Actor;
import com.defty.movie.entity.Director;
import com.defty.movie.entity.Episode;
import com.defty.movie.entity.Movie;
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
import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MovieUserService implements IMovieUserService {

    IMovieRepository movieRepository;
    IMovieRepository movieUserRepository;


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

            Set<Actor> actors=movie.getActors();
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

//    @Override
//    public List<MovieNameResponse> getHistoryMovieSearch(HttpServletRequest request) {
//        String history = CookieUtil.getValue(request, "search_history");
//        if (history == null || history.isEmpty()) {
//            return new ArrayList<>();
//        }
//        history= URLDecoder.decode(history, StandardCharsets.UTF_8);
//        List<String> searchHistorys = new ArrayList<>(Arrays.asList(history.split(",")));
//        List<String> slugs=searchHistorys.subList(0, Math.min(searchHistorys.size(), 3));
//        List<MovieNameResponse> movies=new ArrayList<>();
//        for (String slug : slugs) {
//            Optional<Movie> movie=movieRepository.findBySlugAndStatus(slug,1);
//            if (movie.isPresent()) {
//                MovieNameResponse movieNameResponse = new MovieNameResponse();
//                movieNameResponse.setName(movie.get().getTitle());
//                movieNameResponse.setSlug(slug);
//                movies.add(movieNameResponse);
//            }
//        }
//        return movies;
//    }
}
