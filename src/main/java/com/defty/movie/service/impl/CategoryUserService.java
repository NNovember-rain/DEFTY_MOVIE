package com.defty.movie.service.impl;

import com.defty.movie.dto.response.ApiResponse;
import com.defty.movie.dto.response.CategoryUserResponse;
import com.defty.movie.dto.response.MovieResponse;
import com.defty.movie.dto.response.PageableResponse;
import com.defty.movie.entity.Movie;
import com.defty.movie.exception.NotFoundException;
import com.defty.movie.mapper.MovieMapper;
import com.defty.movie.repository.ICategoryRepository;
import com.defty.movie.repository.IMovieCategoryRepository;
import com.defty.movie.repository.IMovieRepository;
import com.defty.movie.service.ICategoryUserService;
import com.defty.movie.specication.MovieSpecification;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CategoryUserService implements ICategoryUserService {
    private final MovieMapper movieMapper;
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

    @Override
    public ApiResponse<PageableResponse<MovieResponse>> findMoviesByCategory(Pageable pageable, String slug, String nation, String releaseDate, String sortType) {
        Specification<Movie> spec = MovieSpecification.filterByCategoryAndOthers(slug, nation, releaseDate);

        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("createdDate").descending());

        if ("moi-phat-hanh".equalsIgnoreCase(sortType)) {
            sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("releaseDate").descending());
        }
        else if ("xem-nhieu-nhat".equalsIgnoreCase(sortType)) {
//            sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("views").descending());
        }

        Page<Movie> movies = movieRepository.findAll(spec, sortedPageable);

        List<MovieResponse> movieResponseDTOS = new ArrayList<>();
        if (movies.isEmpty()){
            throw new NotFoundException("Not found exception");
        }
        else {
            for(Movie m : movies){
                movieResponseDTOS.add(movieMapper.toMovieResponseDTO(m));
            }
            PageableResponse<MovieResponse> pageableResponse = new PageableResponse<>(movieResponseDTOS, movies.getTotalElements());
            return new ApiResponse<>(200, "OK", pageableResponse);
        }
    }


}
