package com.defty.movie.mapper;

import com.defty.movie.dto.request.DirectorRequest;
import com.defty.movie.dto.response.DirectorResponse;
import com.defty.movie.entity.Director;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DirectorMapper {
    private final ModelMapper modelMapper;
    public Director toDirectorEntity(DirectorRequest directorRequest){
        return modelMapper.map(directorRequest, Director.class);
    }
    public DirectorResponse toDirectorResponseDTO(Director directorEntity){
        return modelMapper.map(directorEntity, DirectorResponse.class);
    }
}
