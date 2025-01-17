package com.defty.movie.service.impl;

import com.defty.movie.dto.response.PageableResponse;
import com.defty.movie.exception.NotFoundException;
import com.defty.movie.utils.SlugUtil;
import com.defty.movie.dto.response.ArticleResponse;
import com.defty.movie.entity.Account;
import com.defty.movie.exception.ImageUploadException;
import com.defty.movie.mapper.ArticleMapper;
import com.defty.movie.dto.request.ArticleRequest;
import com.defty.movie.entity.Article;
import com.defty.movie.repository.IArticleRepository;
import com.defty.movie.service.IArticleService;
import com.defty.movie.utils.UploadImageUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ArticleService implements IArticleService {

    ArticleMapper articleMapper;
    IArticleRepository ariticleRepository;
    AuthService authService;
    UploadImageUtil uploadImageUtil;
    SlugUtil slugUtil;

    String PREFIX_ARTICLE = "ARTICLE | ";

    @Override
    public Integer addArticle(ArticleRequest articleRequest) {
        if(articleRequest.getTitle() == null || articleRequest.getTitle().trim().equals("")) {
            log.error("{}Some fields are missing", PREFIX_ARTICLE);
            throw new RuntimeException("Some fields are missing");
        }
        Article article = articleMapper.toArticleEntity(articleRequest);

        Optional<Account> accountOptional= authService.getCurrentAccount();
        article.setAccount(accountOptional.get());

        Article articleSave=ariticleRepository.save(article);

        articleSave.setSlug(slugUtil.createSlug(articleRequest.getTitle(),articleSave.getId()));
        if(!articleRequest.getThumbnail().isEmpty()) {
            try {
                article.setThumbnail(uploadImageUtil.upload(articleRequest.getThumbnail()));
            } catch (Exception e) {
                log.error("{}Could not upload the image", PREFIX_ARTICLE);
                throw new ImageUploadException("Could not upload the image, please try again later !");
            }
        }

        ariticleRepository.save(articleSave);
        return articleSave.getId();
    }

    @Override
    public void updateArticle(Integer id, ArticleRequest articleRequest) {
        Optional<Article> articleCheck=ariticleRepository.findById(id);
        if(!articleCheck.isPresent()) {
            log.error("{}The article don't exist !", PREFIX_ARTICLE);
            throw new RuntimeException("The article don't exist !");
        }
        log.info(PREFIX_ARTICLE + "Get article by id="+id );
        Article article = articleMapper.toArticleEntity(articleRequest);

        article.setAccount(articleCheck.get().getAccount());
        article.setSlug(slugUtil.createSlug(articleRequest.getTitle(),id));
        article.setId(id);

        if(!articleRequest.getThumbnail().isEmpty()) {
            try {
                article.setThumbnail(uploadImageUtil.upload(articleRequest.getThumbnail()));
            } catch (Exception e) {
                log.error("{}Could not upload the image", PREFIX_ARTICLE);
                throw new ImageUploadException("Could not upload the image, please try again later !");
            }
        }

        ariticleRepository.save(article);
    }

    @Override
    public void deleteArticle(List<Integer> ids) {
        List<Article> articles = ariticleRepository.findAllById(ids);
        log.info(PREFIX_ARTICLE + "Get all article by ids list" );
        if(articles.size()!=ids.size()) {
            log.error("{}Some article not found for delete !", PREFIX_ARTICLE);
            throw new RuntimeException("Some article not found for delete !");
        }
        for(Article article:articles){
            article.setStatus(0);
        }
        ariticleRepository.saveAll(articles);
    }

    @Override
    public ArticleResponse getArticle(Integer id) {
        Optional<Article> article=ariticleRepository.findById(id);
        log.info(PREFIX_ARTICLE + "Get all article by id="+id);
        if(article.isPresent() && article.get().getStatus()==1) {
            return articleMapper.toArticleResponse(article.get());
        }
        else{
            log.error("{}Article not found for delete !", PREFIX_ARTICLE);
            throw new RuntimeException("Article not found for delete !");
        }
    }

    @Override
    public PageableResponse<ArticleResponse> getAllArticles(Pageable pageable, Map<String, Object> params) {
        List<Article> articles= ariticleRepository.findArticles(pageable,params);
        log.info(PREFIX_ARTICLE + "Get all Articles by id list and pagination" );
        List<ArticleResponse> articleResponses=new ArrayList<>();
        if(!articles.isEmpty()) {
            for(Article article:articles) {
                articleResponses.add(articleMapper.toArticleResponse(article));
            }
            return PageableResponse.<ArticleResponse>builder()
                    .content(articleResponses)
                    .totalElements(ariticleRepository.countArticles(params))
                    .build();
        }else {
            log.error("{}Article not found !", PREFIX_ARTICLE);
            throw new NotFoundException(" Article not found !");
        }
    }


}
