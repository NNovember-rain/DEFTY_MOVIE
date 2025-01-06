package com.defty.movie.controller.user;

import com.defty.movie.dto.request.ArticleCommentReactionRequest;
import com.defty.movie.dto.request.ArticleCommentReactionUpdateRequest;
import com.defty.movie.service.IArticleCommentReactionService;
import com.defty.movie.service.impl.ArticleCommentReactionService;
import com.defty.movie.utils.ApiResponeUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/user/article-comment-reaction")
public class ArticleCommentReactionController {

    private final IArticleCommentReactionService articleCommentReactionService;

    @PostMapping()
    public Object addArticleCommentReaction(@RequestBody ArticleCommentReactionRequest articleCommentReactionRequest) {
        Integer id= articleCommentReactionService.addArticleCommentReaction(articleCommentReactionRequest);
        return ApiResponeUtil.ResponseCreatedSuccess(id);
    }


    @PatchMapping()
    public Object updateArticleCommentReaction(@RequestBody ArticleCommentReactionUpdateRequest articleCommentReactionUpdateRequest) {
        articleCommentReactionService.updateArticleCommentReaction(articleCommentReactionUpdateRequest);
        String message="Successfully updated article comment reaction";
        return ApiResponeUtil.ResponseOK(message);
    }

    @DeleteMapping("/{id}")
    public Object deleteArticleCommentReaction(@PathVariable Integer id) {
        articleCommentReactionService.deleteArticleCommentReaction(id);
        String message="Successfully deleted article comment reaction";
        return ApiResponeUtil.ResponseOK(message);
    }

}
