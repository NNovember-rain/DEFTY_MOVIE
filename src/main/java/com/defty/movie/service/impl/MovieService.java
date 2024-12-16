package com.defty.movie.service.impl;

import com.defty.movie.dto.response.PageableResponse;
import com.defty.movie.specication.MovieSpecification;
import com.defty.movie.utils.SlugUtil;
import com.defty.movie.dto.request.MovieRequest;
import com.defty.movie.dto.response.MovieResponse;
import com.defty.movie.entity.Movie;
import com.defty.movie.exception.NotFoundException;
import com.defty.movie.mapper.MovieMapper;
import com.defty.movie.repository.IMovieRepository;
import com.defty.movie.service.IMovieService;
import com.defty.movie.validation.MovieValidation;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


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

        Movie movie = movieMapper.toMovieEntity(movieRequest);
        Movie newMovie = movieRepository.save(movie);
        newMovie.setSlug(slugUtil.createSlug(newMovie.getTitle(), newMovie.getId()));
        movieRepository.save(newMovie);

        return ResponseEntity.ok("Add movie successfully");
    }

    @Override
    public PageableResponse<MovieResponse> getMovies(Pageable pageable, String title, String nation, String releaseDate, Integer ranking, Integer directorId, Integer status) {
        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("createdDate").descending());
        Date sqlReleaseDate = null;
        if (releaseDate != null && !releaseDate.isEmpty()) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                java.util.Date utilDate = sdf.parse(releaseDate);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(utilDate);
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);

                sqlReleaseDate = calendar.getTime();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        Page<Movie> movies = movieRepository.findMovies(title, nation, sqlReleaseDate, ranking, directorId, status, sortedPageable);
        List<MovieResponse> movieResponseDTOS = new ArrayList<>();
        if (movies.isEmpty()){
            throw new NotFoundException("Not found exception");
        }
        else {
            for(Movie m : movies){
                movieResponseDTOS.add(movieMapper.toMovieResponseDTO(m));
            }

            PageableResponse<MovieResponse> pageableResponse = new PageableResponse<>(movieResponseDTOS, movieRepository.count());
            return pageableResponse;
        }
    }

    @Override
    public ResponseEntity<String> updateMovie(Integer id, MovieRequest movieRequest) {
        /*check field*/
        movieValidation.fieldValidation(movieRequest);
        Optional<Movie> movie = movieRepository.findById(id);
        if(movie.isPresent()){
            Movie updatedMovie = movie.get();
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
        List<Movie> movieEntities = movieRepository.findAllById(ids);
        if(movieEntities.size() == 0) throw new NotFoundException("Not found exception");
        for(Movie movie : movieEntities){
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
        Optional<Movie> movie = movieRepository.findById(id);
        if(movie.isPresent()){
            return movieMapper.toMovieResponseDTO(movie.get());
        }
        return "Movie doesn't exist";
    }
}
