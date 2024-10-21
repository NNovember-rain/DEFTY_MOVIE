package com.defty.movie.service;

import com.defty.movie.dto.request.CategoryRequest;
import com.defty.movie.dto.response.CategoryResponse;
import com.defty.movie.dto.response.PageableResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ICategoryService {
    ResponseEntity<String> addCategory(CategoryRequest categoryRequest);
    ResponseEntity<String> updateCategory(Integer id, CategoryRequest categoryRequest);
    ResponseEntity<String> deleteCategory(List<Integer> ids);
    PageableResponse<CategoryResponse> getAllCategorys(Pageable pageable);
    Object getCategory(Integer id);
}
