package com.defty.movie.service.impl;

import com.defty.movie.dto.request.CategoryRequest;
import com.defty.movie.dto.response.ApiResponse;
import com.defty.movie.dto.response.CategoryResponse;
import com.defty.movie.dto.response.MovieResponse;
import com.defty.movie.dto.response.PageableResponse;
import com.defty.movie.entity.Category;
import com.defty.movie.entity.Category;
import com.defty.movie.entity.Movie;
import com.defty.movie.entity.MovieCategory;
import com.defty.movie.exception.CustomDateException;
import com.defty.movie.exception.NotFoundException;
import com.defty.movie.mapper.CategoryMapper;
import com.defty.movie.mapper.MovieMapper;
import com.defty.movie.repository.ICategoryRepository;
import com.defty.movie.repository.IMovieCategoryRepository;
import com.defty.movie.repository.IMovieRepository;
import com.defty.movie.service.ICategoryService;
import com.defty.movie.utils.DateUtil;
import com.defty.movie.utils.SlugUtil;
import com.defty.movie.validation.CategoryValidation;
import jakarta.transaction.Transactional;
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
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CategoryService implements ICategoryService {
    CategoryMapper categoryMapper;
    CategoryValidation categoryValidation;
    ICategoryRepository categoryRepository;
    IMovieCategoryRepository movieCategoryRepository;
    IMovieRepository movieRepository;
    SlugUtil slugUtil;
    DateUtil dateUtil;
    private final MovieMapper movieMapper;

    @Override
    public ApiResponse<Integer> addCategory(CategoryRequest categoryRequest) {
        categoryValidation.fieldValidation(categoryRequest);
        Category categoryEntity = categoryMapper.toCategoryEntity(categoryRequest);
        try {
            Category newCategory = categoryRepository.save(categoryEntity);
            newCategory.setSlug(slugUtil.createSlug(newCategory.getName(), newCategory.getId()));
            categoryRepository.save(newCategory);
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
                updatedCategory.setSlug(slugUtil.createSlug(categoryRequest.getName(), id));
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
        Optional<Category> category = categoryRepository.findById(id);
        if(category.get() != null){
            String message = "";
            if(category.get().getStatus() == 0){
                category.get().setStatus(1);
                message += "Enable categorys successfully";
            }
            else{
                category.get().setStatus(0);
                message += "Disable categorys successfully";
            }
            categoryRepository.save(category.get());
            return new ApiResponse<>(200, message, id);
        }
        else throw new NotFoundException("Not found exception");
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

    @Override
    public ApiResponse<Integer> addMovie(Integer categoryId, List<Integer> movieIds) {
        List<Movie> movies =  movieRepository.findAllById(movieIds);
        Optional<Category> category = categoryRepository.findById(categoryId);
        if (!category.isPresent()) throw new NotFoundException("Category not found exception");
        if(movies.size() == 0) throw new NotFoundException("Movies not found exception");
        for(Movie m : movies){
            MovieCategory movieCategory = new MovieCategory();
            movieCategory.setCategory(category.get());
            movieCategory.setMovie(m);
            MovieCategory oldMovieCategory = movieCategoryRepository.findByCategoryIdAndMovieId(categoryId, m.getId());
            if(oldMovieCategory == null){
                movieCategoryRepository.save(movieCategory);
            }
        }
        return new ApiResponse<>(200, "add movies to category successfully", categoryId);
    }

    @Override
    @Transactional
    public ApiResponse<Integer> deleteMovie(Integer categoryId, List<Integer> movieIds) {
        List<Movie> movies =  movieRepository.findAllById(movieIds);
        Optional<Category> category = categoryRepository.findById(categoryId);
        if (!category.isPresent()) throw new NotFoundException("Category not found exception");
        if(movies.size() == 0) throw new NotFoundException("Movies not found exception");
        for(Integer i : movieIds){
            try {
                movieCategoryRepository.deleteByCategoryIdAndMovieId(categoryId, i);
            }
            catch (Exception e){
                return new ApiResponse<>(200, "having problem while deleting movie from category with message: " + e.getMessage(), categoryId);
            }
        }
        return new ApiResponse<>(200, "delete movies from category successfully", categoryId);
    }

    @Override
    public ApiResponse<PageableResponse<MovieResponse>> findMoviesByCategory(Pageable pageable, Integer categoryId, Boolean isInCategory, String title, String nation, String releaseDate, Integer ranking, Integer directorId) {
        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("createdDate").descending());
        Date startReleaseDate = null;
        Date endReleaseDate = null;
        if (releaseDate != null && !releaseDate.isEmpty()) {
            try{
                String[] dates = releaseDate.split(" - ");
                if (dates.length == 2) {
                    startReleaseDate = dateUtil.stringToSqlDate(dates[0]);
                    endReleaseDate = dateUtil.stringToSqlDate(dates[1]);
                }
                else{
                    throw new CustomDateException("please enter the right date format: dd/MM/yyyy - dd/MM/yyyy");
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
        Page<MovieCategory> movieCategories = categoryRepository.findMoviesByCategory(
                categoryId, isInCategory, title, nation, startReleaseDate, endReleaseDate, ranking, directorId, sortedPageable
        );

        List<MovieResponse> movieResponseDTOS = new ArrayList<>();
        if (movieCategories.isEmpty()){
            throw new NotFoundException("Not found exception");
        }
        else {
            for(MovieCategory m : movieCategories){
                movieResponseDTOS.add(movieMapper.toMovieResponseDTO(m.getMovie()));
            }
            PageableResponse<MovieResponse> pageableResponse = new PageableResponse<>(movieResponseDTOS, movieCategories.getTotalElements());
            return new ApiResponse<>(200, "OK", pageableResponse);
        }
    }
}