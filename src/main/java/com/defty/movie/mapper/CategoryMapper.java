package com.defty.movie.mapper;

import com.defty.movie.dto.request.CategoryRequest;
import com.defty.movie.dto.response.CategoryResponse;
import com.defty.movie.entity.CategoryEntity;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CategoryMapper {
    private final ModelMapper modelMapper;
    public CategoryEntity toCategoryEntity(CategoryRequest categoryRequest){
        return modelMapper.map(categoryRequest, CategoryEntity.class);
    }
    public CategoryResponse toCategoryResponse(CategoryEntity categoryEntity){
        return modelMapper.map(categoryEntity, CategoryResponse.class);
    }
}
