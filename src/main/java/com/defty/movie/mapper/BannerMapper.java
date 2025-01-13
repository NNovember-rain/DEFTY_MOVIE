package com.defty.movie.mapper;

import com.defty.movie.dto.request.BannerRequest;
import com.defty.movie.dto.response.BannerResponse;
import com.defty.movie.entity.Banner;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BannerMapper {
    private final ModelMapper modelMapper;
    public Banner toBannerEntity(BannerRequest actorRequest){
        return modelMapper.map(actorRequest, Banner.class);
    }
    public BannerResponse toBannerResponse(Banner actorEntity){
        return modelMapper.map(actorEntity, BannerResponse.class);
    }
}
