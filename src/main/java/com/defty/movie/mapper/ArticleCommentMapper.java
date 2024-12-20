package com.defty.movie.mapper;

import com.defty.movie.dto.request.ArticleCommentRequest;
import com.defty.movie.dto.response.ArticleCommentResponse;
import com.defty.movie.entity.ArticleComment;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.Comment;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ArticleCommentMapper {

    private final ModelMapper modelMapper;

    public ArticleComment toArticleComment(ArticleCommentRequest articleCommentRequest) {
        return modelMapper.map(articleCommentRequest, ArticleComment.class);
    }

    public ArticleCommentResponse toArticleCommentReSponse(ArticleComment articleComment) {
        return modelMapper.map(articleComment, ArticleCommentResponse.class);
    }
}
