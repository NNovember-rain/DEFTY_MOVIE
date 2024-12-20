package com.defty.movie.controller.user;

import com.defty.movie.dto.request.ArticleCommentRequest;
import com.defty.movie.dto.response.ArticleCommentResponse;
import com.defty.movie.entity.ArticleComment;
import com.defty.movie.service.IArticleCommentService;
import com.defty.movie.service.impl.ArticleCommentService;
import com.defty.movie.utils.ApiResponeUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/user/articlecomment")
public class ArticleCommentController {

    private final IArticleCommentService articleCommentService;

    @PostMapping()
    public Object addArticleComment(@RequestBody ArticleCommentRequest articleCommentRequest) {
        Integer id= articleCommentService.addArticleComment(articleCommentRequest);
        return ApiResponeUtil.ResponseCreatedSuccess(id);
    }

    @PatchMapping("/{id}")
    public Object updateArticleComment(@PathVariable Integer id,@RequestBody ArticleCommentRequest articleCommentRequest) {
        articleCommentService.updateArticleComment(id, articleCommentRequest);
        String massage="Updated article comment";
        return ApiResponeUtil.ResponseOK(massage);
    }

    @DeleteMapping("/{ids}")
    public Object deletarArticleComment(@PathVariable List<Integer> ids) {
        articleCommentService.deleteArticleComment(ids);
        String massage="Deleted article comment";
        return ApiResponeUtil.ResponseOK(massage);
    }

    @GetMapping("/{id}")
    public Object getArticleComment(@PathVariable Integer id) {
        List<ArticleCommentResponse> articleComments= articleCommentService.getArticleComment(id);
        return ApiResponeUtil.ResponseOK(articleComments);
    }
}
