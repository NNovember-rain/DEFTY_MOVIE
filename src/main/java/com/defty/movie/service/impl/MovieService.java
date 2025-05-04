package com.defty.movie.service.impl;

import com.defty.movie.dto.request.MovieRequest;
import com.defty.movie.dto.response.ActorResponse;
import com.defty.movie.dto.response.ApiResponse;
import com.defty.movie.dto.response.MovieResponse;
import com.defty.movie.dto.response.PageableResponse;
import com.defty.movie.entity.Actor;
import com.defty.movie.entity.MovieCategory;
import com.defty.movie.exception.CustomDateException;
import com.defty.movie.exception.MediaUploadException;
import com.defty.movie.exception.NotFoundException;
import com.defty.movie.mapper.ActorMapper;
import com.defty.movie.mapper.MovieMapper;
import com.defty.movie.entity.Director;
import com.defty.movie.entity.Movie;
import com.defty.movie.repository.IActorRepository;
import com.defty.movie.repository.IDirectorRepository;
import com.defty.movie.repository.IMovieCategoryRepository;
import com.defty.movie.repository.IMovieRepository;
import com.defty.movie.service.IMovieService;
import com.defty.movie.utils.DateUtil;
import com.defty.movie.utils.SlugUtil;
import com.defty.movie.utils.UploadImageUtil;
import com.defty.movie.utils.UploadVideoUtil;
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

import java.util.*;
import java.util.stream.Collectors;


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
    UploadVideoUtil uploadVideoUtil;
    DateUtil dateUtil;
    IActorRepository actorRepository;
    ActorMapper actorMapper;

    @Override
    public ApiResponse<Integer> addMovie(MovieRequest movieRequest) {
        /*check field*/
        movieValidation.fieldValidation(movieRequest);

        Movie movie = movieMapper.toMovieEntity(movieRequest);
        Optional<Director> director = directorRepository.findById(movieRequest.getDirectorId());
        director.ifPresent(movie::setDirector);
        Movie newMovie = movieRepository.save(movie);
        newMovie.setSlug(slugUtil.createSlug(newMovie.getTitle(), newMovie.getId()));

        if(movieRequest.getTrailer()!=null) {
            try {
                newMovie.setTrailer(uploadVideoUtil.upload(movieRequest.getTrailer()));
            } catch (Exception e) {
                throw new MediaUploadException("Could not upload the video, please try again later!");
            }
        }

        if(movieRequest.getThumbnail()!=null) {
            try {
                newMovie.setThumbnail(uploadImageUtil.upload(movieRequest.getThumbnail()));
            } catch (Exception e) {
                return new ApiResponse<>(500, e.getMessage(), newMovie.getId());
            }
        }

        if(movieRequest.getCoverImage()!=null) {
            try {
                newMovie.setCoverImage(uploadImageUtil.upload(movieRequest.getCoverImage()));
            } catch (Exception e) {
                return new ApiResponse<>(500, e.getMessage(), newMovie.getId());
            }
        }

        movieRepository.save(newMovie);

        return new ApiResponse<>(201, "created", newMovie.getId());
    }

    @Override
    public ApiResponse<PageableResponse<MovieResponse>> getMovies(Pageable pageable, String title, String nation, String releaseDate, Integer ranking, Integer directorId, Integer status) {
        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("createdDate").descending());
        Date startReleaseDate = null;
        Date endReleaseDate = null;
        if (releaseDate != null && !releaseDate.isEmpty()) {
            try{
                String[] dates = releaseDate.split(" - ");
                if (dates.length == 2) {
                    startReleaseDate = dateUtil.stringToSqlDate(dates[0]);
                    endReleaseDate = dateUtil.stringToSqlDate(dates[1]);
                }
                else{
                    throw new CustomDateException("please enter the right date format: dd/MM/yyyy - dd/MM/yyyy");
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
        Page<Movie> movies = movieRepository.findMovies(
                title, nation, startReleaseDate, endReleaseDate, ranking, directorId, status, sortedPageable
        );

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

            if (movieRequest.getThumbnail() != null && !movieRequest.getThumbnail().isEmpty()) {
                try {
                    updatedMovie.setThumbnail(uploadImageUtil.upload(movieRequest.getThumbnail()));
                }
                catch (Exception e){
                    throw new MediaUploadException("Could not upload the image, please try again later!" + e);
                }
            }

            if (movieRequest.getCoverImage() != null && !movieRequest.getCoverImage().isEmpty()) {
                try {
                    updatedMovie.setCoverImage(uploadImageUtil.upload(movieRequest.getCoverImage()));
                }
                catch (Exception e){
                    throw new MediaUploadException("Could not upload the image, please try again later!" + e);
                }
            }

            if(movieRequest.getTrailer() != null && !movieRequest.getTrailer().isEmpty()) {
                try {
                    updatedMovie.setTrailer(uploadVideoUtil.upload(movieRequest.getTrailer()));
                } catch (Exception e) {
                    throw new MediaUploadException("Could not upload the video, please try again later! " + e);
                }
            }

            Optional<Director> director = directorRepository.findById(movieRequest.getDirectorId());
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
            try{
                movieRepository.save(movie.get());
            }
            catch (Exception e){
                return new ApiResponse<>(200, e.getMessage(), id);
            }
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

    @Override
    public Object getEpisodeOfMovieDetails(Integer episodeId) {
        return null;
    }

    @Override
    public ApiResponse<Integer> addActor(Integer movieId, List<Integer> ids) {
        Optional<Movie> movieOpt = movieRepository.findById(movieId);
        if (movieOpt.isEmpty()) {
            throw new NotFoundException("Movie not found");
        }

        List<Actor> actorsToAdd = actorRepository.findAllById(ids);
        if (actorsToAdd.isEmpty()) {
            throw new NotFoundException("Actors not found");
        }

        Movie movie = movieOpt.get();
        List<Actor> currentActors = movie.getActors();
        
        Set<Integer> currentActorIds = currentActors.stream()
                .map(Actor::getId)
                .collect(Collectors.toSet());

        List<Actor> filteredToAdd = actorsToAdd.stream()
                .filter(actor -> !currentActorIds.contains(actor.getId()))
                .collect(Collectors.toList());

        currentActors.addAll(filteredToAdd);
        movie.setActors(currentActors);

        try {
            movieRepository.save(movie);
            return new ApiResponse<>(200, "Add actors to movie successfully", movieId);
        } catch (Exception e) {
            return new ApiResponse<>(500, "Exception | " + e.getMessage(), movieId);
        }
    }


    @Override
    public ApiResponse<Integer> deleteActor(Integer movieId, List<Integer> ids) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new NotFoundException("Movie isn't existed"));

        List<Actor> updatedList = movie.getActors().stream()
                .filter(actor -> !ids.contains(actor.getId()))
                .collect(Collectors.toList());

        movie.setActors(updatedList);

        movieRepository.save(movie);

        return new ApiResponse<>(200, "delete actors from movie successfully", movieId);
    }

    @Override
    public ApiResponse<PageableResponse<ActorResponse>> findActorsByMovie(Pageable pageable, Integer movieId, String name, String gender, String date_of_birth, String nationality) {
        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("createdDate").descending());

        Date startDate = null;
        Date endDate = null;
        if (date_of_birth != null && !date_of_birth.isEmpty()) {
            try {
                String[] dates = date_of_birth.split(" - ");
                if (dates.length == 2) {
                    startDate = dateUtil.stringToSqlDate(dates[0]);
                    endDate = dateUtil.stringToSqlDate(dates[1]);
                }
                else{
                    throw new CustomDateException("please enter the right date format: dd/MM/yyyy - dd/MM/yyyy");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Page<Actor> actorEntities = movieRepository.findActorByMovieId(movieId,
                name, gender, startDate, endDate, nationality, sortedPageable
        );

        List<ActorResponse> actorResponseDTOS = new ArrayList<>();
        if (actorEntities.isEmpty()){
            throw new NotFoundException("Not found exception");
        }
        else {
            for(Actor d : actorEntities){
                actorResponseDTOS.add(actorMapper.toActorResponse(d));
            }

            PageableResponse<ActorResponse> pageableResponse= new PageableResponse<>(actorResponseDTOS, actorEntities.getTotalElements());
            ApiResponse<PageableResponse<ActorResponse>> apiResponse = new ApiResponse<>(200, "OK", pageableResponse);
            return apiResponse;
        }
    }

    @Override
    public ApiResponse<PageableResponse<ActorResponse>> findActorsNotInMovie(Pageable pageable, Integer movieId, String name, String gender, String date_of_birth, String nationality) {
        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("createdDate").descending());

        Date startDate = null;
        Date endDate = null;
        if (date_of_birth != null && !date_of_birth.isEmpty()) {
            try {
                String[] dates = date_of_birth.split(" - ");
                if (dates.length == 2) {
                    startDate = dateUtil.stringToSqlDate(dates[0]);
                    endDate = dateUtil.stringToSqlDate(dates[1]);
                }
                else{
                    throw new CustomDateException("please enter the right date format: dd/MM/yyyy - dd/MM/yyyy");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Page<Actor> actorEntities = movieRepository.findActorNotInMovie(movieId,
                name, gender, startDate, endDate, nationality, sortedPageable
        );

        List<ActorResponse> actorResponseDTOS = new ArrayList<>();
        if (actorEntities.isEmpty()){
            throw new NotFoundException("Not found exception");
        }
        else {
            for(Actor d : actorEntities){
                actorResponseDTOS.add(actorMapper.toActorResponse(d));
            }

            PageableResponse<ActorResponse> pageableResponse= new PageableResponse<>(actorResponseDTOS, actorEntities.getTotalElements());
            ApiResponse<PageableResponse<ActorResponse>> apiResponse = new ApiResponse<>(200, "OK", pageableResponse);
            return apiResponse;
        }
    }


}
