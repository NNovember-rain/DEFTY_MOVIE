package com.defty.movie.mapper;

import com.defty.movie.dto.request.ArticleCommentRequest;
import com.defty.movie.dto.response.ArticleCommentReactionResponse;
import com.defty.movie.dto.response.ArticleCommentResponse;
import com.defty.movie.entity.ArticleComment;
import com.defty.movie.entity.ArticleCommentReaction;
import com.defty.movie.repository.IArticleCommentReactionRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.Comment;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ArticleCommentMapper {

    private final ModelMapper modelMapper;
    private final IArticleCommentReactionRepository articleCommentReactionRepository;
    private final ArticleCommentReactionMapper articleCommentReactionMapper;

    public ArticleComment toArticleComment(ArticleCommentRequest articleCommentRequest) {
        return modelMapper.map(articleCommentRequest, ArticleComment.class);
    }

    public ArticleCommentResponse toArticleCommentResponse(ArticleComment articleComment) {
        List<ArticleCommentReaction> articleCommentReactions= articleCommentReactionRepository.findByArticleCommentId(articleComment.getId());
        ArticleCommentResponse articleCommentResponse=modelMapper.map(articleComment, ArticleCommentResponse.class);
        List<ArticleCommentReactionResponse> articleCommentReactionResponses=new ArrayList<>();
        for(ArticleCommentReaction a: articleCommentReactions){
            articleCommentReactionResponses.add(articleCommentReactionMapper.toArticleCommentReactionResponse(a));
        }
        articleCommentResponse.setArticleCommentReactionResponses(articleCommentReactionResponses);
        return articleCommentResponse;
    }
}
