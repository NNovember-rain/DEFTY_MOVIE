package com.defty.movie.service.impl;

import com.defty.movie.Util.SlugUtil;
import com.defty.movie.dto.request.MovieRequest;
import com.defty.movie.dto.response.MovieResponseDTO;
import com.defty.movie.entity.MovieEntity;
import com.defty.movie.exception.NotFoundException;
import com.defty.movie.mapper.MovieMapper;
import com.defty.movie.repository.IMovieRepository;
import com.defty.movie.service.IMovieService;
import com.defty.movie.validation.MovieValidation;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MovieService implements IMovieService {
    private final MovieMapper movieMapper;
    private final MovieValidation movieValidation;
    private final IMovieRepository movieRepository;
    private final SlugUtil slugUtil;

    @Override
    public ResponseEntity<String> addMovie(MovieRequest movieRequest) {
        /*check field*/
        movieValidation.fieldValidation(movieRequest);

        MovieEntity movie = movieMapper.toMovieEntity(movieRequest);
        MovieEntity newMovie = movieRepository.save(movie);
        newMovie.setSlug(slugUtil.createSlug(newMovie.getTitle(), newMovie.getId()));
        movieRepository.save(newMovie);

        return ResponseEntity.ok("Add movie successfully");
    }

    @Override
    public List<MovieResponseDTO> getMovies() {
        List<MovieEntity> movieEntities = movieRepository.findAll();
        List<MovieResponseDTO> movieResponseDTOS = new ArrayList<>();
        for(MovieEntity movie : movieEntities){
            movieResponseDTOS.add(movieMapper.toMovieResponseDTO(movie));
        }
        return movieResponseDTOS;
    }

    @Override
    public ResponseEntity<String> updateMovie(Integer id, MovieRequest movieRequest) {
        /*check field*/
        movieValidation.fieldValidation(movieRequest);
        Optional<MovieEntity> movie = movieRepository.findById(id);
        if(movie.isPresent()){
            MovieEntity updatedMovie = movie.get();
            /*copy different fields from movieRequest to updatedMovie*/
            BeanUtils.copyProperties(movieRequest, updatedMovie, "id");
            updatedMovie.setSlug(slugUtil.createSlug(movieRequest.getTitle(), id));

            movieRepository.save(updatedMovie);
        }
        else {
            throw new NotFoundException("Not found exception");
        }
        return ResponseEntity.ok("Update movie successfully");
    }

    @Override
    public ResponseEntity<String> deleteMovie(List<Integer> ids) {
        List<MovieEntity> movieEntities = movieRepository.findAllById(ids);
        if(movieEntities.size() == 0) throw new NotFoundException("Not found exception");
        for(MovieEntity movie : movieEntities){
            movie.setStatus(0);
        }
        movieRepository.saveAll(movieEntities);
        if(ids.size() > 1){
            return ResponseEntity.ok("Update movies successfully");
        }
        return ResponseEntity.ok("Update movie successfully");
    }

    @Override
    public Object getMovie(Integer id) {
        Optional<MovieEntity> movie = movieRepository.findById(id);
        if(movie.isPresent()){
            return movieMapper.toMovieResponseDTO(movie.get());
        }
        return "Movie doesn't exist";
    }
}
