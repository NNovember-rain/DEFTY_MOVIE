package com.defty.movie.service.impl;

import com.defty.movie.mapper.ArticleMapper;
import com.defty.movie.dto.request.ArticleRequest;
import com.defty.movie.entity.Article;
import com.defty.movie.repository.IAriticleRepository;
import com.defty.movie.service.IArticleService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ArticleService implements IArticleService {

    private final ArticleMapper articleMapper;
    private final IAriticleRepository ariticleRepository;
    private final ModelMapper modelMapper;

    @Override
    public void addArticle(ArticleRequest articleRequest) {
        Article article = articleMapper.toArticleEntity(articleRequest);
        ariticleRepository.save(article);
    }

    @Override
    public void updateArticle(Integer id, ArticleRequest articleRequest) {
        Article articleCheck=ariticleRepository.findById(id).orElseThrow(()->new RuntimeException("The article dont exist !"));
        Article article = articleMapper.toArticleEntity(articleRequest);
        article.setId(id);
        ariticleRepository.save(article);
    }

    @Override
    public void deleteArticle(List<Integer> ids) {
        List<Article> articles = ariticleRepository.findAllById(ids);
        if(articles.size()!=ids.size()) throw new RuntimeException("Some user not found for delete !");
        ariticleRepository.deleteAll(articles);

    }
}
