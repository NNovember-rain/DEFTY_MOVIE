package com.defty.movie.mapper;

import com.defty.movie.dto.request.CategoryRequest;
import com.defty.movie.dto.response.CategoryResponse;
import com.defty.movie.entity.Category;
import com.defty.movie.entity.MovieCategory;
import com.defty.movie.repository.IMovieCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CategoryMapper {
    private final ModelMapper modelMapper;
    private final IMovieCategoryRepository movieCategoryRepository;
    public Category toCategoryEntity(CategoryRequest categoryRequest){
        return modelMapper.map(categoryRequest, Category.class);
    }
    public CategoryResponse toCategoryResponse(Category categoryEntity){
        CategoryResponse categoryResponse = modelMapper.map(categoryEntity, CategoryResponse.class);
        categoryResponse.setNumberOfMovies(categoryEntity.getMovieCategories().size());
        return categoryResponse;
    }
}
