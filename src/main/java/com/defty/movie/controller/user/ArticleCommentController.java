package com.defty.movie.controller.user;

import com.defty.movie.dto.request.ArticleCommentRequest;
import com.defty.movie.service.impl.ArticleCommentService;
import com.defty.movie.utils.ApiResponeUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/user/articlecomment")
public class ArticleCommentController {

    private final ArticleCommentService articleCommentService;

    @PostMapping()
    public Object addArticleComment(@RequestBody ArticleCommentRequest articleCommentRequest) {
        Integer id= articleCommentService.addArticleComment(articleCommentRequest);
        return ApiResponeUtil.ResponseOK(id);
    }
}
