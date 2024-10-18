package com.defty.movie.controller.admin;

import com.defty.movie.utils.ApiResponeUtil;
import com.defty.movie.dto.request.ArticleRequest;
import com.defty.movie.dto.response.ArticlePageableResponse;
import com.defty.movie.dto.response.ArticleResponse;
import com.defty.movie.service.impl.ArticleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/admin")
public class ArticleController {
    private final ArticleService articleService;

    @PostMapping("/article")
    @PreAuthorize("@requiredPermission.checkPermission('CREATE_ARTICLE')")
    public ResponseEntity<Integer> addArticle(@RequestBody ArticleRequest articleRequest) {
        Integer id=articleService.addArticle(articleRequest);
        return  ApiResponeUtil.ResponseOK(id);
    }


    @PatchMapping("/article/{id}")
    @PreAuthorize("@requiredPermission.checkPermission('UPDATE_ARTICLE')")
    public ResponseEntity<String> updateArticle( @PathVariable Integer id,
                                            @RequestBody ArticleRequest articleRequest) {
        articleService.updateArticle(id,articleRequest);
        String responseMessage="Update successful";
        return  ApiResponeUtil.ResponseOK(responseMessage);
    }

    @DeleteMapping("/article/{ids}")
    @PreAuthorize("@requiredPermission.checkPermission('DELETE_ARTICLE')")
    public ResponseEntity<String> deleteArticle(@PathVariable List<Integer> ids) {
        articleService.deleteArticle(ids);
        String responseMessage="Delete successful";
        return ApiResponeUtil.ResponseOK(responseMessage);
    }

    @GetMapping("/articles")
    @PreAuthorize("@requiredPermission.checkPermission('GET_ARTICLE')")
    public ResponseEntity<?> getArticle(@RequestParam(required = false) Integer id,
                                        Pageable pageable) {
        if (id != null) {
            ArticleResponse articleResponse=articleService.getArticle(id);
            return  ApiResponeUtil.ResponseOK(articleResponse);
        }else {
            Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("createdDate").descending());
            List<ArticleResponse> articleResponses=articleService.getAllArticles(sortedPageable);
            ArticlePageableResponse articlePageableResponse = ArticlePageableResponse.builder()
                    .articleResponses(articleResponses)
                    .totalElements(articleService.getArticleCount())
                    .build();
            return ApiResponeUtil.ResponseOK(articlePageableResponse);
        }
    }

}
