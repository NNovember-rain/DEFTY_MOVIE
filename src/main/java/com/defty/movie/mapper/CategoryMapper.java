package com.defty.movie.mapper;

import com.defty.movie.dto.request.CategoryRequest;
import com.defty.movie.dto.response.CategoryResponse;
import com.defty.movie.entity.Category;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CategoryMapper {
    private final ModelMapper modelMapper;
    public Category toCategoryEntity(CategoryRequest categoryRequest){
        return modelMapper.map(categoryRequest, Category.class);
    }
    public CategoryResponse toCategoryResponse(Category categoryEntity){
        return modelMapper.map(categoryEntity, CategoryResponse.class);
    }
}
