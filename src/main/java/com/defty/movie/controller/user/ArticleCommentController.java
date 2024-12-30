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
    String PREFIX_ARTICLE_COMMENT = "ARTICLE_COMMENT | ";

    @PostMapping()
    public Object addArticleComment(@RequestBody ArticleCommentRequest articleCommentRequest) {
        Integer id= articleCommentService.addArticleComment(articleCommentRequest);
        log.info(PREFIX_ARTICLE_COMMENT + "Add Aritcle Comment successfully");
        return ApiResponeUtil.ResponseCreatedSuccess(id);
    }

    @PatchMapping("/{id}")
    public Object updateArticleComment(@PathVariable Integer id,@RequestBody ArticleCommentRequest articleCommentRequest) {
        articleCommentService.updateArticleComment(id, articleCommentRequest);
        String message="Updated article comment";
        log.info(PREFIX_ARTICLE_COMMENT + "Update Aritcle Comment successfully");
        return ApiResponeUtil.ResponseOK(message);
    }

    @DeleteMapping("/{ids}")
    public Object deletarArticleComment(@PathVariable List<Integer> ids) {
        articleCommentService.deleteArticleComment(ids);
        String message="Deleted article comment";
        log.info(PREFIX_ARTICLE_COMMENT + "Deleted Article Comment successfully");
        return ApiResponeUtil.ResponseOK(message);
    }

    @GetMapping("/{articleid}")
    public Object getArticleComment(@PathVariable Integer articleid) {
        List<ArticleCommentResponse> articleComments= articleCommentService.getArticleComment(articleid);
        log.info(PREFIX_ARTICLE_COMMENT + "Get All Article Comment with ArticleId successfully");
        return ApiResponeUtil.ResponseOK(articleComments);
    }
}
