package com.defty.movie.controller.admin;

import com.defty.movie.dto.response.PageableResponse;
import com.defty.movie.utils.ApiResponeUtil;
import com.defty.movie.dto.request.ArticleRequest;
import com.defty.movie.dto.response.ArticleResponse;
import com.defty.movie.service.impl.ArticleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/admin")
public class ArticleController {
    private final ArticleService articleService;

    @PostMapping("/article")
    @PreAuthorize("@requiredPermission.checkPermission('CREATE_ARTICLE')")
    public ResponseEntity<Integer> addArticle( @RequestBody ArticleRequest articleRequest,
                                               @RequestPart(value = "thumbnail", required = false) MultipartFile thumbnail) {
        Integer id=articleService.addArticle(articleRequest);
        return ApiResponeUtil.ResponseOK(id);
    }


    @PatchMapping("/article/{id}")
    @PreAuthorize("@requiredPermission.checkPermission('UPDATE_ARTICLE')")
    public ResponseEntity<String> updateArticle( @PathVariable Integer id,
                                                 @RequestBody ArticleRequest articleRequest) {
        articleService.updateArticle(id,articleRequest);
        String responseMessage="Update successful";
        return ApiResponeUtil.ResponseOK(responseMessage);
    }

    @DeleteMapping("/article/{ids}")
    @PreAuthorize("@requiredPermission.checkPermission('DELETE_ARTICLE')")
    public ResponseEntity<String> deleteArticle(@PathVariable List<Integer> ids) {
        articleService.deleteArticle(ids);
        String responseMessage="Delete successful";
        return ApiResponeUtil.ResponseOK(responseMessage);
    }

    @GetMapping("/article/{id}")
    @PreAuthorize("@requiredPermission.checkPermission('GET_ARTICLE')")
    public ResponseEntity<?> getArticle(@PathVariable Integer id) {
        ArticleResponse articleResponse=articleService.getArticle(id);
        return ApiResponeUtil.ResponseOK(articleResponse);
    }

    @GetMapping("/articles")
    @PreAuthorize("@requiredPermission.checkPermission('GET_ARTICLE')")
    public ResponseEntity<?> getArticles(@Valid @RequestParam(value = "page", defaultValue = "0") int page,
                                         @RequestParam(value = "size", defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        PageableResponse<ArticleResponse> articlePageableResponse=articleService.getAllArticles(pageable);
        return ApiResponeUtil.ResponseOK(articlePageableResponse);
    }

    @GetMapping("/articles/search")
    @PreAuthorize("@requiredPermission.checkPermission('GET_ARTICLE')")
    public ResponseEntity<?> findArticles(Pageable pageable,@RequestParam Map<String, Object> params) {
        PageableResponse<ArticleResponse> articlePageableResponse=articleService.findArticles(pageable,params);
        return ApiResponeUtil.ResponseOK(articlePageableResponse);
    }

}
