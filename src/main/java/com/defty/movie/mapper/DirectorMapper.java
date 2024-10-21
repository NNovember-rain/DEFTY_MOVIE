package com.defty.movie.mapper;

import com.defty.movie.dto.request.DirectorRequest;
import com.defty.movie.dto.response.DirectorResponse;
import com.defty.movie.entity.DirectorEntity;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DirectorMapper {
    private final ModelMapper modelMapper;
    public DirectorEntity toDirectorEntity(DirectorRequest directorRequest){
        return modelMapper.map(directorRequest, DirectorEntity.class);
    }
    public DirectorResponse toDirectorResponseDTO(DirectorEntity directorEntity){
        return modelMapper.map(directorEntity, DirectorResponse.class);
    }
}
