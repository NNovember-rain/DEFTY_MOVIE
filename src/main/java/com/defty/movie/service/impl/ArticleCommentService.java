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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ArticleCommentService implements IArticleCommentService {

    private final IAuthUserService authUserService;
    private final IArticleRepository articleRepository;
    private final IArticleCommentRepository articleCommentRepository;
    private final ArticleCommentMapper articleCommentMapper;

    String PREFIX_ARTICLE_COMMENT= "ARTICLE_COMMENT | ";

    @Override
    public Integer addArticleComment(ArticleCommentRequest articleCommentRequest) {
        ArticleComment articleComment = new ArticleComment();
        Optional<User> user = authUserService.getCurrentUser();
        if(user.isPresent()) {
            log.info(PREFIX_ARTICLE_COMMENT + "Get current user success");
            articleComment.setUser(user.get());
        }else{
            log.error("{}User not found", PREFIX_ARTICLE_COMMENT);
            throw new NotFoundException("User not found");
        }

        Optional<Article> article = articleRepository.findById(articleCommentRequest.getArticleId());
        if(article.isPresent()) {
            articleComment.setArticle(article.get());
            log.info(PREFIX_ARTICLE_COMMENT + "Get article by articleId="+articleCommentRequest.getArticleId()+ " success");
        }else {
            log.error("{}Article not found", PREFIX_ARTICLE_COMMENT);
            throw new NotFoundException("Article not found");
        }

        if(articleCommentRequest.getParentArticleCommentId()!=null) {
            Optional<ArticleComment> articleCommentParent= articleCommentRepository.findById(articleCommentRequest.getParentArticleCommentId());
            if(articleCommentParent.isPresent()) {
                log.info(PREFIX_ARTICLE_COMMENT + "Get ArticleCommentParent by articleCommentParentId="+articleCommentRequest.getParentArticleCommentId()+ " success");
                articleComment.setParentArticleComment(articleCommentParent.get());
            }else {
                log.error("{}Parent article comment not found", PREFIX_ARTICLE_COMMENT);
                throw new NotFoundException("Parent article comment not found");
            }
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
            log.info(PREFIX_ARTICLE_COMMENT + "Get article comment by articleId="+articleCommentRequest.getArticleId()+ " success");
            ArticleComment articleCommentUpdate = articleComment.get();
            BeanUtils.copyProperties(articleCommentRequest,articleCommentUpdate);
            articleCommentRepository.save(articleCommentUpdate);
        }else {
            log.error("{}Article Comment not found", PREFIX_ARTICLE_COMMENT);
            throw new NotFoundException("Article Comment not found");
        }
    }

    @Override
    public void deleteArticleComment(List<Integer> ids) {
        List<ArticleComment> articleComments = articleCommentRepository.findAllById(ids);
        if(articleComments.size()!=ids.size()) {
            log.error("{}Some Article Comment not found", PREFIX_ARTICLE_COMMENT);
            throw new NotFoundException("Some Article Comment not found");
        }
        else {
            // xoa "cmt" bao gom ca "cmt con"
            for(ArticleComment articleComment:articleComments) {
                if(articleComment.getParentArticleComment()==null) { // check xem cmt nay la cha hay con
                    Optional<List<ArticleComment>> articleCommentSub= articleCommentRepository.findByParentArticleComment_Id(articleComment.getId());
                    log.info(PREFIX_ARTICLE_COMMENT + "Get article comment by articleId="+articleComment.getId()+ " success");
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
    public List<ArticleCommentResponse> getArticleComment(Integer articleId) {
        Optional<List<ArticleComment>> articleComments = articleCommentRepository.findByArticleIdAndStatus(articleId, 1);
        if(articleComments.isPresent() && articleComments.get().size()>0) {
            List<ArticleComment> articleCommentList = articleComments.get();
            List<ArticleCommentResponse> articleCommentResponses = new ArrayList<>();
            for(ArticleComment articleComment:articleCommentList) {
                articleCommentResponses.add(articleCommentMapper.toArticleCommentResponse(articleComment));
            }
            return articleCommentResponses;
        }else {
            log.error("{}Article Comment not found", PREFIX_ARTICLE_COMMENT);
            throw new NotFoundException("ArticleComment not found");
        }

    }

    @Override
    public ArticleComment getArticleCommentById(Integer id) {
        return articleCommentRepository.findById(id).orElseThrow(() -> new RuntimeException("Article Commnent not found"));
    }
}
