package com.defty.movie.mapper;

import com.defty.movie.dto.request.MovieRequest;
import com.defty.movie.dto.response.MovieResponse;
import com.defty.movie.entity.MovieEntity;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MovieMapper {
    private final ModelMapper modelMapper;

    public MovieEntity toMovieEntity(MovieRequest movieRequest){
        MovieEntity movie = modelMapper.map(movieRequest, MovieEntity.class);
        return movie;
    }
    public MovieResponse toMovieResponseDTO(MovieEntity movie){
        MovieResponse movieResponseDTO = modelMapper.map(movie, MovieResponse.class);
        return movieResponseDTO;
    }
}
