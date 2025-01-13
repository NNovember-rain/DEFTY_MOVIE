package com.defty.movie.mapper;

import com.defty.movie.dto.response.CommentReactionResponse;
import com.defty.movie.entity.ArticleCommentReaction;
import com.defty.movie.repository.IArticleCommentReactionRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ArticleCommentReactionMapper {

    private final ModelMapper modelMapper;
    private final IArticleCommentReactionRepository articleCommentReactionRepository;
    private final UserMapper userMapper;

    public CommentReactionResponse toArticleCommentReactionResponse(ArticleCommentReaction articleCommentReaction) {
        CommentReactionResponse commentReactionResponse = new CommentReactionResponse();
        commentReactionResponse.setContent(articleCommentReaction.getContent());
        commentReactionResponse.setUserResponse(userMapper.toUserResponse(articleCommentReaction.getUser()));
        return commentReactionResponse;
    }
}
