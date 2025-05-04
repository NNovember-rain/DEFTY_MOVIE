package com.defty.movie.service.impl;

import com.defty.movie.dto.response.CategoryUserResponse;
import com.defty.movie.dto.response.MovieResponse;
import com.defty.movie.entity.Movie;
import com.defty.movie.repository.ICategoryRepository;
import com.defty.movie.repository.IMovieCategoryRepository;
import com.defty.movie.repository.IMovieRepository;
import com.defty.movie.service.ICategoryUserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CategoryUserService implements ICategoryUserService {

    ICategoryRepository categoryRepository;
    IMovieRepository movieRepository;

    @Override
    public CategoryUserResponse getCategory() {
        List<String> regions = movieRepository.findAllNations();
        List<Integer> releaseDates = movieRepository.findDistinctReleaseYearsOfActiveMovies();
        List<String> categories = categoryRepository.findAllCategories();
        List<String> paidCategories = new ArrayList<>();
        paidCategories.add("Premium");
        paidCategories.add("Normal");
        return CategoryUserResponse.builder()
                .regions(regions)
                .releaseDates(releaseDates)
                .categories(categories)
                .paidCategories(paidCategories)
                .build();
    }

    public List<MovieResponse> searchCategory(String category, String region, Integer paidCategory, Integer releaseYear) {
        List<Movie> movies = movieRepository.searchByAnyFilter(category, region, paidCategory, releaseYear);
        List<MovieResponse> movieResponses = new ArrayList<>();
        for (Movie movie : movies) {
            MovieResponse movieResponse = new MovieResponse();
            movieResponse.setId(movie.getId());
            movieResponse.setTitle(movie.getTitle());
            movieResponse.setReleaseDate(movie.getReleaseDate());
            movieResponse.setNation(movie.getNation());
            movieResponse.setThumbnail(movie.getThumbnail());
            movieResponse.setSlug(movie.getSlug());
            movieResponse.setMembershipType(movie.getMembershipType());
            movieResponses.add(movieResponse);
        }
        return movieResponses;
    }
}
