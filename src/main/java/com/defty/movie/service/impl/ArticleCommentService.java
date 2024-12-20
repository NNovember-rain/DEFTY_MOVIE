package com.defty.movie.service.impl;

import com.defty.movie.dto.request.ArticleCommentRequest;
import com.defty.movie.entity.ArticleComment;
import com.defty.movie.entity.User;
import com.defty.movie.exception.NotFoundException;
import com.defty.movie.service.IArticleCommentService;
import com.defty.movie.service.IAuthService;
import com.defty.movie.service.IAuthUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ArticleCommentService implements IArticleCommentService {

    private final IAuthUserService authUserService;

    @Override
    public Integer addArticleComment(ArticleCommentRequest articleCommentRequest) {
        ArticleComment articleComment = new ArticleComment();
        Optional<User> user = authUserService.getCurrentUser();
        if(user.isPresent()) {
            articleComment.setUser(user.get());
        }else throw new NotFoundException("User not found");


        return null;
    }
}
