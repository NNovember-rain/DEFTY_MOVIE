package com.defty.movie.mapper;

import com.defty.movie.dto.request.CollectionRequest;
import com.defty.movie.dto.response.CollectionResponse;
import com.defty.movie.entity.Collection;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CollectionMapper {
    private final ModelMapper modelMapper;
    public Collection toCollectionEntity(CollectionRequest collectionRequest){
        return modelMapper.map(collectionRequest, Collection.class);
    }
    public CollectionResponse toCollectionResponse(Collection collectionEntity){
        CollectionResponse collectionResponse = modelMapper.map(collectionEntity, CollectionResponse.class);
        return collectionResponse;
    }
}
