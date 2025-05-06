package com.defty.movie.controller.user;

import com.defty.movie.dto.response.*;
import com.defty.movie.entity.Movie;
import com.defty.movie.service.ICategoryUserService;
import com.defty.movie.service.IMovieService;
import com.defty.movie.utils.ApiResponeUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/user/accessible/category")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CategoryUserController {
    ICategoryUserService categoryUserService;

    @GetMapping("")
    public ResponseEntity<?> getCategory() {
        CategoryUserResponse categoryUserResponse = categoryUserService.getCategory();
        ApiResponse<CategoryUserResponse> response = ApiResponse.<CategoryUserResponse>builder()
                .status(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .data(categoryUserResponse)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchMovies(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String region,
            @RequestParam(required = false) Integer releaseYear,
            @RequestParam(required = false) Integer paidCategory) {

        List<MovieResponse> result = categoryUserService.searchCategory(category, region, paidCategory, releaseYear);
        ApiResponse<List<MovieResponse>> response = ApiResponse.<List<MovieResponse>>builder()
                .status(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .data(result)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    @GetMapping("/movies")
    public ResponseEntity<ApiResponse<PageableResponse<MovieResponse>>> getMoviesByCategory(
            @RequestParam String slug,
            @RequestParam(required = false) String nation,
            @RequestParam(required = false) String releaseDate,
            @RequestParam(required = false) String sortType,
            Pageable pageable) {
        return ResponseEntity.ok(categoryUserService.findMoviesByCategory(pageable, slug, nation, releaseDate, sortType));
    }
}
