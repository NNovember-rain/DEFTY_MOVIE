package com.defty.movie.mapper;

import com.defty.movie.dto.request.ShowonRequest;
import com.defty.movie.dto.response.ShowonResponse;
import com.defty.movie.entity.Showon;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ShowonMapper {
    ModelMapper modelMapper;
    public Showon toShowonEntity(ShowonRequest showonRequest){
        return modelMapper.map(showonRequest, Showon.class);
    }
    public ShowonResponse toShowonResponse(Showon showon){
        ShowonResponse showonResponse = modelMapper.map(showon, ShowonResponse.class);
        showonResponse.setContentId(showon.getCategory().getId());
        return showonResponse;
    }
}
