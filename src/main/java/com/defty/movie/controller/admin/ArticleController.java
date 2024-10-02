package com.defty.movie.controller.admin;

import com.defty.movie.common.ApiStatus;
import com.defty.movie.dto.request.ArticleRequest;
import com.defty.movie.dto.response.ApiResponse;
import com.defty.movie.dto.response.ArticleResponse;
import com.defty.movie.service.impl.ArticleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("${api.prefix}")
public class ArticleController {
    private final ArticleService articleService;

    @PostMapping("/article")
    public ResponseEntity<ApiResponse<ArticleResponse>> addArticle(@RequestBody ArticleRequest articleRequest) {
        try {
            articleService.addArticle(articleRequest);
            ApiResponse<ArticleResponse> apiResponse = ApiResponse.<ArticleResponse>builder()
                    .message("Success")
                    .status(ApiStatus.SUCCESS.getCode())
                    .message(ApiStatus.SUCCESS.getMessage())
                    .data(new ArticleResponse("Article added successfully!"))
                    .build();
            return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
        } catch(Exception e){
            log.error("Article addition failed: {}", e.getMessage(), e);
            ApiResponse<ArticleResponse> response = ApiResponse.<ArticleResponse>builder()
                    .status(ApiStatus.INTERNAL_ERROR.getCode())
                    .message(ApiStatus.INTERNAL_ERROR.getMessage())
                    .data(new ArticleResponse("Article addition failed !"))
                    .build();

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }

    @PostMapping("/article/{id}")
    public ResponseEntity<ApiResponse<ArticleResponse>> updateArticle(@PathVariable Integer id,
                                                                      @RequestBody ArticleRequest articleRequest) {
        articleService.updateArticle(id,articleRequest);
        ApiResponse<ArticleResponse> apiResponse = ApiResponse.<ArticleResponse>builder()
                .message("Success")
                .status(ApiStatus.SUCCESS.getCode())
                .message(ApiStatus.SUCCESS. getMessage())
                .data(new ArticleResponse("Edited the article successfully!"))
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @DeleteMapping("/article/{id}")
    public ResponseEntity<ApiResponse<ArticleResponse>> deleteArticle(@PathVariable Integer id) {
        articleService.deleteArticle(id);
        ApiResponse<ArticleResponse> apiResponse = ApiResponse.<ArticleResponse>builder()
                .message("Success")
                .status(ApiStatus.SUCCESS.getCode())
                .message(ApiStatus.SUCCESS. getMessage())
                .data(new ArticleResponse("Deleted the article successfully!"))
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }
}
