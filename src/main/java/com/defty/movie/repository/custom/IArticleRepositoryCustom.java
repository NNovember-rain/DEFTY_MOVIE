package com.defty.movie.repository.custom;

import com.defty.movie.entity.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

public interface IArticleRepositoryCustom {
    List<Article> findArticles(Pageable pageable, Map<String, Object> params);
    Long countArticles(Map<String, Object> params);
}
