package com.defty.movie.service.impl;

import com.defty.movie.dto.request.MovieRequest;
import com.defty.movie.dto.response.ApiResponse;
import com.defty.movie.dto.response.MovieResponse;
import com.defty.movie.dto.response.PageableResponse;
import com.defty.movie.entity.Director;
import com.defty.movie.entity.Movie;
import com.defty.movie.exception.NotFoundException;
import com.defty.movie.mapper.MovieMapper;
import com.defty.movie.repository.IDirectorRepository;
import com.defty.movie.repository.IMovieRepository;
import com.defty.movie.service.IMovieService;
import com.defty.movie.utils.SlugUtil;
import com.defty.movie.utils.UploadImageUtil;
import com.defty.movie.validation.MovieValidation;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    private final IDirectorRepository directorRepository;
    private final SlugUtil slugUtil;
    UploadImageUtil uploadImageUtil;

    @Override
    public ApiResponse<Integer> addMovie(MovieRequest movieRequest) {
        /*check field*/
        movieValidation.fieldValidation(movieRequest);

        Movie movie = movieMapper.toMovieEntity(movieRequest);
        Optional<Director> director = directorRepository.findByFullName(movieRequest.getDirector());
        director.ifPresent(movie::setDirector);
        Movie newMovie = movieRepository.save(movie);
        newMovie.setSlug(slugUtil.createSlug(newMovie.getTitle(), newMovie.getId()));
        try{
            newMovie.setThubnail(uploadImageUtil.upload(movieRequest.getThumbnail()));
            newMovie.setCoverImage(uploadImageUtil.upload(movieRequest.getCoverImage()));
            movieRepository.save(newMovie);
        }
        catch (Exception e){
            return new ApiResponse<>(500, e.getMessage(), newMovie.getId());
        }
        return new ApiResponse<>(201, "created", newMovie.getId());
    }

    @Override
    public ApiResponse<PageableResponse<MovieResponse>> getMovies(Pageable pageable, String title, String nation, String releaseDate, Integer ranking, Integer directorId, Integer status) {
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
            PageableResponse<MovieResponse> pageableResponse = new PageableResponse<>(movieResponseDTOS, movies.getTotalElements());
            return new ApiResponse<>(200, "OK", pageableResponse);
        }
    }

    @Override
    public ApiResponse<Integer> updateMovie(Integer id, MovieRequest movieRequest) {
        /*check field*/
        movieValidation.fieldValidation(movieRequest);
        Optional<Movie> movie = movieRepository.findById(id);
        if(movie.isPresent()){
            Movie updatedMovie = movie.get();
            /*copy different fields from movieRequest to updatedMovie*/
            BeanUtils.copyProperties(movieRequest, updatedMovie, "id");
            updatedMovie.setSlug(slugUtil.createSlug(movieRequest.getTitle(), id));
            Optional<Director> director = directorRepository.findByFullName(movieRequest.getDirector());
            director.ifPresent(updatedMovie::setDirector);
            movieRepository.save(updatedMovie);
        }
        else {
            throw new NotFoundException("Not found exception");
        }
        return new ApiResponse<>(200, "Update movie successfully", id);
    }

    @Override
    public ApiResponse<List<Integer>> deleteMovie(List<Integer> ids) {
        List<Movie> movieEntities = movieRepository.findAllById(ids);
        if(movieEntities.isEmpty()) throw new NotFoundException("Not found exception");
        for(Movie movie : movieEntities){
            movie.setStatus(-1);
        }
        movieRepository.saveAll(movieEntities);
        if(ids.size() > 1){
            return new ApiResponse<>(200, "Delete movies successfully", ids);
        }
        return new ApiResponse<>(200, "Delete movie successfully", ids);
    }
    @Override
    public ApiResponse<Integer> changeStatus(Integer id) {
        Optional<Movie> movie = movieRepository.findById(id);
        if(movie.get() != null){
            String message = "";
            if(movie.get().getStatus() == 0){
                movie.get().setStatus(1);
                message += "Enable movies successfully";
            }
            else{
                movie.get().setStatus(0);
                message += "Disable movies successfully";
            }
            movieRepository.save(movie.get());
            return new ApiResponse<>(200, message, id);
        }
        else throw new NotFoundException("Not found exception");
    }

    @Override
    public Object getMovie(Integer id) {
        Optional<Movie> movie = movieRepository.findById(id);
        if(movie.isPresent()){
            return new ApiResponse<>(200, "OK", movieMapper.toMovieResponseDTO(movie.get()));
        }
        return new ApiResponse<>(200, "Movie doesn't exist", null);
    }
}
