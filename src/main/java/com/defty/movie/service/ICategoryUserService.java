package com.defty.movie.service;

import com.defty.movie.dto.response.CategoryUserResponse;
import com.defty.movie.dto.response.MovieResponse;

import java.util.List;

public interface ICategoryUserService {
    CategoryUserResponse getCategory();
    List<MovieResponse> searchCategory(String category, String region, Integer paidCategory, Integer releaseDate);
}
