package com.defty.movie.service.impl;

import com.defty.movie.mapper.ArticleConverter;
import com.defty.movie.dto.request.ArticleRequest;
import com.defty.movie.entity.Article;
import com.defty.movie.repository.IAriticleRepository;
import com.defty.movie.service.IArticleService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ArticleService implements IArticleService {

    private final ArticleConverter articleConverter;
    private final IAriticleRepository ariticleRepository;
    private final ModelMapper modelMapper;

    @Override
    public void addArticle(ArticleRequest articleRequest) {
        Article article = articleConverter.ArticleRequestToArticleEntity(articleRequest);
        ariticleRepository.save(article);
    }

    @Override
    public void updateArticle(Integer id, ArticleRequest articleRequest) {
        Article article = articleConverter.ArticleRequestToArticleEntity(articleRequest);
        article.setId(id);
        ariticleRepository.save(article);
    }

    @Override
    public void deleteArticle(Integer id) {
        Article article = ariticleRepository.findById(id).orElse(null);
        article.setStatus(0);
        ariticleRepository.save(article);
    }
}
