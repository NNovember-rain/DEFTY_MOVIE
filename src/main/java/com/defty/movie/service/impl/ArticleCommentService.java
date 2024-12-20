package com.defty.movie.service.impl;

import com.defty.movie.dto.request.ArticleCommentRequest;
import com.defty.movie.dto.response.ArticleCommentResponse;
import com.defty.movie.entity.Article;
import com.defty.movie.entity.ArticleComment;
import com.defty.movie.entity.User;
import com.defty.movie.exception.FieldRequiredException;
import com.defty.movie.exception.NotFoundException;
import com.defty.movie.mapper.ArticleCommentMapper;
import com.defty.movie.repository.IArticleCommentRepository;
import com.defty.movie.repository.IArticleRepository;
import com.defty.movie.service.IArticleCommentService;
import com.defty.movie.service.IArticleService;
import com.defty.movie.service.IAuthService;
import com.defty.movie.service.IAuthUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ArticleCommentService implements IArticleCommentService {

    private final IAuthUserService authUserService;
    private final IArticleRepository articleRepository;
    private final IArticleCommentRepository articleCommentRepository;
    private final ArticleCommentMapper articleCommentMapper;

    @Override
    public Integer addArticleComment(ArticleCommentRequest articleCommentRequest) {
        ArticleComment articleComment = new ArticleComment();
        Optional<User> user = authUserService.getCurrentUser();
        if(user.isPresent()) {
            articleComment.setUser(user.get());
        }else throw new NotFoundException("User not found");

        Optional<Article> article = articleRepository.findById(articleCommentRequest.getArticleId());
        if(article.isPresent()) {
            articleComment.setArticle(article.get());
        }else throw new NotFoundException("Article not found");

        if(articleCommentRequest.getParentArticleCommentId()!=null) {
            Optional<ArticleComment> articleCommentParent= articleCommentRepository.findById(articleCommentRequest.getParentArticleCommentId());
            if(articleCommentParent.isPresent()) {
                articleComment.setParentArticleComment(articleCommentParent.get());
            }else throw new NotFoundException("Parent article comment not found");
        }

        articleComment.setStatus(1);

        articleComment.setContent(articleCommentRequest.getContent());

        ArticleComment result=articleCommentRepository.save(articleComment);

        return result.getId();
    }

    @Override
    public void updateArticleComment(Integer id, ArticleCommentRequest articleCommentRequest) {
        Optional<ArticleComment> articleComment = articleCommentRepository.findById(id);
        if(articleComment.isPresent()) {
            ArticleComment articleCommentUpdate = articleComment.get();
            BeanUtils.copyProperties(articleCommentRequest,articleCommentUpdate);
            articleCommentRepository.save(articleCommentUpdate);
        }else throw new NotFoundException("Article Comment not found");
    }

    @Override
    public void deleteArticleComment(List<Integer> ids) {
        List<ArticleComment> articleComments = articleCommentRepository.findAllById(ids);
        if(articleComments.size()!=ids.size()) throw new NotFoundException("Some Article Comment not found");
        else {
            // xoa "cmt" bao gom ca "cmt con"
            for(ArticleComment articleComment:articleComments) {
                if(articleComment.getParentArticleComment()==null) { // check xem cmt nay la cha hay con
                    Optional<List<ArticleComment>> articleCommentSub= articleCommentRepository.findByParentArticleComment_Id(articleComment.getId());
                    if(articleCommentSub.isPresent() && articleCommentSub.get().size()>0) {
                        List<ArticleComment> articleCommentSubUpdates = articleCommentSub.get();
                        for(ArticleComment articleCommentUpdate:articleCommentSubUpdates) {
                            articleCommentUpdate.setStatus(0);
                        }
                        articleCommentRepository.saveAll(articleCommentSubUpdates);
                    }
                }
                articleComment.setStatus(0);
            }
            articleCommentRepository.saveAll(articleComments);
        }
    }

    @Override
    public List<ArticleCommentResponse> getArticleComment(Integer ariticleId) {
        Optional<List<ArticleComment>> articleComments = articleCommentRepository.findByArticleIdAndStatus(ariticleId, 1);
        if(articleComments.isPresent() && articleComments.get().size()>0) {
            List<ArticleComment> articleCommentList = articleComments.get();
            List<ArticleCommentResponse> articleCommentResponses = new ArrayList<>();
            for(ArticleComment articleComment:articleCommentList) {
                articleCommentResponses.add(articleCommentMapper.toArticleCommentReSponse(articleComment));
            }
            return articleCommentResponses;
        }throw new NotFoundException("ArticleComment not found");

    }
}
