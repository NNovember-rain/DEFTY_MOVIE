package com.defty.movie.service.impl;

import com.defty.movie.utils.SlugUtil;
import com.defty.movie.dto.response.ArticlePageableResponse;
import com.defty.movie.dto.response.ArticleResponse;
import com.defty.movie.entity.Account;
import com.defty.movie.exception.ImageUploadException;
import com.defty.movie.mapper.ArticleMapper;
import com.defty.movie.dto.request.ArticleRequest;
import com.defty.movie.entity.Article;
import com.defty.movie.repository.IAriticleRepository;
import com.defty.movie.service.IArticleService;
import com.defty.movie.utils.UploadImageUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ArticleService implements IArticleService {

    ArticleMapper articleMapper;
    IAriticleRepository ariticleRepository;
    AccountService accountService;
    UploadImageUtil uploadImageUtil;
    SlugUtil slugUtil;

    @Override
    public Integer addArticle(ArticleRequest articleRequest) {
        if(articleRequest.getTitle() == null || articleRequest.getTitle().trim().equals("")) throw new RuntimeException("Some fields are missing");
        Article article = articleMapper.toArticleEntity(articleRequest);

        Optional<Account> accountOptional= accountService.getCurrentAccount();
        article.setAccount(accountOptional.get());

        Article articleSave=ariticleRepository.save(article);

        articleSave.setSlug(slugUtil.createSlug(articleRequest.getTitle(),articleSave.getId()));

        try {
            article.setThumbnail(uploadImageUtil.upload(articleRequest.getThumbnail()));
        }catch (Exception e){
            throw new ImageUploadException("Could not upload the image, please try again later !");
        }

        ariticleRepository.save(articleSave);
        return articleSave.getId();
    }

    @Override
    public void updateArticle(Integer id, ArticleRequest articleRequest) {
        Article articleCheck=ariticleRepository.findById(id).orElseThrow(()->new RuntimeException("The article dont exist !"));
        Article article = articleMapper.toArticleEntity(articleRequest);

        article.setSlug(slugUtil.createSlug(articleRequest.getTitle(),id));
        article.setCreatedDate(articleCheck.getCreatedDate());
        article.setAccount(articleCheck.getAccount());
        article.setSlug(slugUtil.createSlug(articleRequest.getTitle(),id));
        article.setId(id);

        ariticleRepository.save(article);
    }

    @Override
    public void deleteArticle(List<Integer> ids) {
        List<Article> articles = ariticleRepository.findAllById(ids);
        if(articles.size()!=ids.size()) {
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
        if(article.isPresent() && article.get().getStatus()==1) {
            return articleMapper.toArticleResponse(article.get());
        }
        else{
            throw new RuntimeException("Article not found for delete !");
        }
    }



    @Override
    public ArticlePageableResponse getAllArticles(Pageable pageable) {
        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("createdDate").descending());
        Optional<Page<Article>> articles= Optional.ofNullable(ariticleRepository.findAllByStatus(1,sortedPageable));
        List<ArticleResponse> articleResponses=new ArrayList<>();
        if(!articles.get().isEmpty()) {
            for(Article article:articles.get()) {
                articleResponses.add(articleMapper.toArticleResponse(article));
            }

            return ArticlePageableResponse.builder()
                    .articleResponses(articleResponses)
                    .totalElements(ariticleRepository.count())
                    .build();
        }else throw new RuntimeException(" Article not found !");
    }

    @Override
    public Long getArticleCount() {
        return ariticleRepository.count();
    }
}
