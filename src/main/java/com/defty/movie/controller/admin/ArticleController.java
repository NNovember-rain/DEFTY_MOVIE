package com.defty.movie.controller.admin;

import com.defty.movie.dto.response.PageableResponse;
import com.defty.movie.service.IArticleService;
import com.defty.movie.utils.ApiResponeUtil;
import com.defty.movie.dto.request.ArticleRequest;
import com.defty.movie.dto.response.ArticleResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/admin/article")

public class ArticleController {

    private final IArticleService articleService;

    @PostMapping()
    @PreAuthorize("@requiredPermission.checkPermission('CREATE_ARTICLE')")
    public ResponseEntity<Integer> addArticle( @ModelAttribute ArticleRequest articleRequest) {
        Integer id=articleService.addArticle(articleRequest);
        return ApiResponeUtil.ResponseCreatedSuccess(id);
    }


    @PatchMapping("/{id}")
    @PreAuthorize("@requiredPermission.checkPermission('UPDATE_ARTICLE')")
    public ResponseEntity<String> updateArticle( @PathVariable Integer id,
                                                 @ModelAttribute ArticleRequest articleRequest) {
        articleService.updateArticle(id,articleRequest);
        String responseMessage="Updated successful";
        return ApiResponeUtil.ResponseOK(responseMessage);
    }

    @DeleteMapping("/{ids}")
    @PreAuthorize("@requiredPermission.checkPermission('DELETE_ARTICLE')")
    public ResponseEntity<String> deleteArticle(@PathVariable List<Integer> ids) {
        articleService.deleteArticle(ids);
        String responseMessage="Delete successful";
        return ApiResponeUtil.ResponseOK(responseMessage);
    }

    @GetMapping("/{id}")
    @PreAuthorize("@requiredPermission.checkPermission('GET_ARTICLE')")
    public ResponseEntity<?> getArticle(@PathVariable Integer id) {
        ArticleResponse articleResponse=articleService.getArticle(id);
        return ApiResponeUtil.ResponseOK(articleResponse);
    }

    @GetMapping()
    @PreAuthorize("@requiredPermission.checkPermission('GET_ARTICLE')")
    public ResponseEntity<?> getArticles(@Valid @RequestParam(value = "page", defaultValue = "0") int page,
                                         @RequestParam(value = "size", defaultValue = "10") int size,
                                         @RequestParam Map<String, Object> params) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        PageableResponse<ArticleResponse> articlePageableResponse = articleService.getAllArticles(pageable, params);
        return ApiResponeUtil.ResponseOK(articlePageableResponse);
    }

}
