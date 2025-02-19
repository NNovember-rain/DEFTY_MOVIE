package com.defty.movie.mapper;

import com.defty.movie.dto.request.ActorRequest;
import com.defty.movie.dto.response.ActorResponse;
import com.defty.movie.entity.Actor;
import com.defty.movie.utils.DateUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ActorMapper {
    ModelMapper modelMapper;
    DateUtil dateUtil;
    public Actor toActorEntity(ActorRequest actorRequest){
        return modelMapper.map(actorRequest, Actor.class);
    }
    public ActorResponse toActorResponse(Actor actorEntity){
        return modelMapper.map(actorEntity, ActorResponse.class);
    }
}
