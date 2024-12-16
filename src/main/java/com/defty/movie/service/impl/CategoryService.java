package com.defty.movie.service.impl;

import com.defty.movie.dto.request.CategoryRequest;
import com.defty.movie.dto.response.CategoryResponse;
import com.defty.movie.dto.response.PageableResponse;
import com.defty.movie.entity.Actor;
import com.defty.movie.entity.Category;
import com.defty.movie.exception.NotFoundException;
import com.defty.movie.mapper.CategoryMapper;
import com.defty.movie.repository.ICategoryRepository;
import com.defty.movie.service.ICategoryService;
import com.defty.movie.validation.CategoryValidation;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CategoryService implements ICategoryService {
    CategoryMapper categoryMapper;
    CategoryValidation categoryValidation;
    ICategoryRepository categoryRepository;

    @Override
    public ResponseEntity<String> addCategory(CategoryRequest categoryRequest) {
        categoryValidation.fieldValidation(categoryRequest);
        Category categoryEntity = categoryMapper.toCategoryEntity(categoryRequest);
        try {
            categoryRepository.save(categoryEntity);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return ResponseEntity.ok("Add category successfully");
    }

    @Override
    public ResponseEntity<String> updateCategory(Integer id, CategoryRequest categoryRequest) {
        categoryValidation.fieldValidation(categoryRequest);
        Optional<Category> categoryEntity = categoryRepository.findById(id);
        if(categoryEntity.isPresent()){
            Category updatedCategory = categoryEntity.get();
            BeanUtils.copyProperties(categoryRequest, updatedCategory, "id");
            try {
                categoryRepository.save(updatedCategory);
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
        else{
            throw new NotFoundException("Not found exception");

        }
        return ResponseEntity.ok("Update category successfully");
    }

    @Override
    public ResponseEntity<String> deleteCategory(List<Integer> ids) {
        List<Category> categories = categoryRepository.findAllById(ids);
        if(categories.size() == 0) throw new NotFoundException("Not found exception");
        for(Category category : categories){
            category.setStatus(0);
        }
        categoryRepository.saveAll(categories);
        if(ids.size() > 1){
            return ResponseEntity.ok("Update categories successfully");
        }
        return ResponseEntity.ok("Update category successfully");
    }

    @Override
    public PageableResponse<CategoryResponse> getAllCategories(Pageable pageable, String name, Integer status) {
        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("createdDate").descending());
        Page<Category> categories = categoryRepository.findCategories(name, status, sortedPageable);
        List<CategoryResponse> categoryResponses = new ArrayList<>();
        if (categories.isEmpty()){
            throw new NotFoundException("Not found exception");
        }
        else {
            for(Category c : categories){
                categoryResponses.add(categoryMapper.toCategoryResponse(c));
            }

            PageableResponse<CategoryResponse> pageableResponse= new PageableResponse<>(categoryResponses, categoryRepository.count());
            return pageableResponse;
        }
    }

    @Override
    public Object getCategory(Integer id) {
        return null;
    }
}
