package com.defty.movie.service;

import com.defty.movie.dto.response.ApiResponse;
import com.defty.movie.dto.response.CategoryUserResponse;
import com.defty.movie.dto.response.MovieResponse;
import com.defty.movie.dto.response.PageableResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ICategoryUserService {
    CategoryUserResponse getCategory();
    List<MovieResponse> searchCategory(String category, String region, Integer paidCategory, Integer releaseDate);
    ApiResponse<PageableResponse<MovieResponse>> findMoviesByCategory(Pageable pageable, String slug, String nation, String releaseDate, String sortType);
}
