package com.defty.movie.service.impl;

import com.defty.movie.dto.request.CategoryRequest;
import com.defty.movie.dto.response.CategoryResponse;
import com.defty.movie.dto.response.PageableResponse;
import com.defty.movie.service.ICategoryService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService implements ICategoryService {
    @Override
    public ResponseEntity<String> addCategory(CategoryRequest categoryRequest) {
        return null;
    }

    @Override
    public ResponseEntity<String> updateCategory(Integer id, CategoryRequest categoryRequest) {
        return null;
    }

    @Override
    public ResponseEntity<String> deleteCategory(List<Integer> ids) {
        return null;
    }

    @Override
    public PageableResponse<CategoryResponse> getAllCategorys(Pageable pageable) {
        return null;
    }

    @Override
    public Object getCategory(Integer id) {
        return null;
    }
}
