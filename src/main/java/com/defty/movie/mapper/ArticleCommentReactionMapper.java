package com.defty.movie.mapper;

import com.defty.movie.dto.response.ArticleCommentReactionResponse;
import com.defty.movie.entity.ArticleComment;
import com.defty.movie.entity.ArticleCommentReaction;
import com.defty.movie.repository.IArticleCommentReactionRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ArticleCommentReactionMapper {

    private final ModelMapper modelMapper;
    private final IArticleCommentReactionRepository articleCommentReactionRepository;
    private final UserMapper userMapper;

    public ArticleCommentReactionResponse toArticleCommentReactionResponse(ArticleCommentReaction articleCommentReaction) {
        ArticleCommentReactionResponse articleCommentReactionResponse = new ArticleCommentReactionResponse();
        articleCommentReactionResponse.setContent(articleCommentReaction.getContent());
        articleCommentReactionResponse.setUserResponse(userMapper.toUserResponse(articleCommentReaction.getUser()));
        return articleCommentReactionResponse;
    }
}
