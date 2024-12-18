package com.defty.movie.service;

import com.defty.movie.dto.request.CategoryRequest;
import com.defty.movie.dto.response.ApiResponse;
import com.defty.movie.dto.response.CategoryResponse;
import com.defty.movie.dto.response.PageableResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ICategoryService {
    ApiResponse<Integer> addCategory(CategoryRequest categoryRequest);
    ApiResponse<Integer> updateCategory(Integer id, CategoryRequest categoryRequest);
    ApiResponse<List<Integer>> deleteCategory(List<Integer> ids);
    ApiResponse<PageableResponse<CategoryResponse>> getAllCategories(Pageable pageable, String name, Integer status);
    Object getCategory(Integer id);
}
