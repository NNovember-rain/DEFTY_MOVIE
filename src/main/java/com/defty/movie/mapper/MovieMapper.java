package com.defty.movie.mapper;

import com.defty.movie.dto.request.MovieRequest;
import com.defty.movie.dto.response.MovieResponse;
import com.defty.movie.entity.Movie;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MovieMapper {
    private final ModelMapper modelMapper;

    public Movie toMovieEntity(MovieRequest movieRequest){
        Movie movie = modelMapper.map(movieRequest, Movie.class);
        return movie;
    }
    public MovieResponse toMovieResponseDTO(Movie movie){
        MovieResponse movieResponseDTO = modelMapper.map(movie, MovieResponse.class);
        movieResponseDTO.setDirector(movie.getDirector().getFullName());
        movieResponseDTO.setThumbnail(movie.getThumbnail());
        return movieResponseDTO;
    }
}
