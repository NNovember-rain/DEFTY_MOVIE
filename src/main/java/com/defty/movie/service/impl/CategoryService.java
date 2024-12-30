package com.defty.movie.service.impl;

import com.defty.movie.dto.request.CategoryRequest;
import com.defty.movie.dto.response.ApiResponse;
import com.defty.movie.dto.response.CategoryResponse;
import com.defty.movie.dto.response.PageableResponse;
import com.defty.movie.entity.Category;
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
    public ApiResponse<Integer> addCategory(CategoryRequest categoryRequest) {
        categoryValidation.fieldValidation(categoryRequest);
        Category categoryEntity = categoryMapper.toCategoryEntity(categoryRequest);
        try {
            categoryRepository.save(categoryEntity);
        }
        catch (Exception e){
            return new ApiResponse<>(500, e.getMessage(), categoryEntity.getId());
        }
        return new ApiResponse<>(201, "created", categoryEntity.getId());
    }

    @Override
    public ApiResponse<Integer> updateCategory(Integer id, CategoryRequest categoryRequest) {
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
        return new ApiResponse<>(200, "Update category successfully", id);
    }

    @Override
    public ApiResponse<List<Integer>> deleteCategory(List<Integer> ids) {
        List<Category> categories = categoryRepository.findAllById(ids);
        if(categories.size() == 0) throw new NotFoundException("Not found exception");
        for(Category category : categories){
            category.setStatus(-1);
        }
        categoryRepository.saveAll(categories);
        if(ids.size() > 1){
            return new ApiResponse<>(200, "Delete categories successfully", ids);
        }
        return new ApiResponse<>(200, "Delete categorie successfully", ids);

    }

    @Override
    public ApiResponse<Integer> changeStatus(Integer id) {
        Optional<Category> actor = categoryRepository.findById(id);
        if(actor.get() != null){
            String message = "";
            if(actor.get().getStatus() == 0){
                actor.get().setStatus(1);
                message += "Enable actors successfully";
            }
            else{
                actor.get().setStatus(0);
                message += "Disable actors successfully";
            }
            categoryRepository.save(actor.get());
            return new ApiResponse<>(200, message, id);
        }
        else throw new NotFoundException("Not found exception");
    }

    @Override
    public ApiResponse<List<Integer>> enableCategory(List<Integer> ids) {
        List<Category> categories = categoryRepository.findAllById(ids);
        if(categories.size() == 0) throw new NotFoundException("Not found exception");
        for(Category category : categories){
            category.setStatus(1);
        }
        categoryRepository.saveAll(categories);
        if(ids.size() > 1){
            return new ApiResponse<>(200, "Enable categories successfully", ids);
        }
        return new ApiResponse<>(200, "Enable categorie successfully", ids);

    }

    @Override
    public ApiResponse<List<Integer>> disableCategory(List<Integer> ids) {
        List<Category> categories = categoryRepository.findAllById(ids);
        if(categories.size() == 0) throw new NotFoundException("Not found exception");
        for(Category category : categories){
            category.setStatus(0);
        }
        categoryRepository.saveAll(categories);
        if(ids.size() > 1){
            return new ApiResponse<>(200, "Disable categories successfully", ids);
        }
        return new ApiResponse<>(200, "Disable categorie successfully", ids);

    }

    @Override
    public ApiResponse<PageableResponse<CategoryResponse>> getAllCategories(Pageable pageable, String name, Integer status) {
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

            PageableResponse<CategoryResponse> pageableResponse= new PageableResponse<>(categoryResponses, categories.getTotalElements());
            return new ApiResponse<>(200, "OK", pageableResponse);
        }
    }

    @Override
    public Object getCategory(Integer id) {
        Optional<Category> category = categoryRepository.findById(id);
        if(category.isPresent()){
            return new ApiResponse<>(200, "OK", categoryMapper.toCategoryResponse(category.get()));
        }
        return new ApiResponse<>(404, "Category doesn't exist", null);
    }
}