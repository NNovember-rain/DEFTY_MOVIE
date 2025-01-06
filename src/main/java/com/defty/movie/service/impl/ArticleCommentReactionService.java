package com.defty.movie.service.impl;

import com.defty.movie.dto.request.ArticleCommentReactionRequest;
import com.defty.movie.dto.request.ArticleCommentReactionUpdateRequest;
import com.defty.movie.entity.ArticleComment;
import com.defty.movie.entity.ArticleCommentReaction;
import com.defty.movie.entity.User;
import com.defty.movie.exception.NotFoundException;
import com.defty.movie.repository.IArticleCommentReactionRepository;
import com.defty.movie.service.IArticleCommentReactionService;
import com.defty.movie.service.IArticleCommentService;
import com.defty.movie.service.IAuthUserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ArticleCommentReactionService implements IArticleCommentReactionService {

    private final IAuthUserService authUserService;
    private final IArticleCommentService articleCommentService;
    private final IArticleCommentReactionRepository articleCommentReactionRepository;

    @Override
    public Integer addArticleCommentReaction(ArticleCommentReactionRequest articleCommentReactionRequest) {

        ArticleCommentReaction articleCommentReaction = new ArticleCommentReaction();
        Optional<User> user = authUserService.getCurrentUser();
        if(user.isPresent()) {
            articleCommentReaction.setUser(user.get());
        }else{
            throw new NotFoundException("User not found");
        }

        ArticleComment articleComment=articleCommentService.getArticleCommentById(articleCommentReactionRequest.getArticleCommentId());

        articleCommentReaction.setArticleComment(articleComment);

        articleCommentReaction.setContent(articleCommentReactionRequest.getContent());

        articleCommentReaction.setUser(user.get());

        articleCommentReaction.setStatus(1);

        ArticleCommentReaction articleCommentReactionSaved=articleCommentReactionRepository.save(articleCommentReaction);

        return articleCommentReactionSaved.getId();
    }

    @Override
    public void updateArticleCommentReaction(ArticleCommentReactionUpdateRequest articleCommentReactionUpdateRequest) {
        ArticleCommentReaction articleCommentReaction = articleCommentReactionRepository.findById(articleCommentReactionUpdateRequest.getArticleCommentReactionId())
                .orElseThrow(() -> new NotFoundException("ArticleCommentReaction not found"));
        articleCommentReaction.setContent(articleCommentReactionUpdateRequest.getContent());
        articleCommentReactionRepository.save(articleCommentReaction);
    }

    @Override
    public void deleteArticleCommentReaction(Integer articleCommentReactionId) {
        ArticleCommentReaction articleCommentReaction = articleCommentReactionRepository.findById(articleCommentReactionId)
                .orElseThrow(() -> new NotFoundException("ArticleCommentReaction with id not found"));
        articleCommentReaction.setStatus(0);
        articleCommentReactionRepository.save(articleCommentReaction);
    }


}
