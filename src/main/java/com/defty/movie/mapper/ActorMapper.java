package com.defty.movie.mapper;

import com.defty.movie.dto.request.ActorRequest;
import com.defty.movie.dto.response.ActorResponse;
import com.defty.movie.entity.ActorEntity;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ActorMapper {
    private final ModelMapper modelMapper;
    public ActorEntity toActorEntity(ActorRequest actorRequest){
        return modelMapper.map(actorRequest, ActorEntity.class);
    }
    public ActorResponse toActorResponse(ActorEntity actorEntity){
        return modelMapper.map(actorEntity, ActorResponse.class);
    }
}
