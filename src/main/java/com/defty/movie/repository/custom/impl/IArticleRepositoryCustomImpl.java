package com.defty.movie.repository.custom.impl;

import com.defty.movie.entity.Article;
import com.defty.movie.repository.custom.IArticleRepositoryCustom;
import com.defty.movie.utils.NumberUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;


@Repository
public class IArticleRepositoryCustomImpl implements IArticleRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<Article> findArticles(Map<String, Object> params, Pageable pageable) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT a.* FROM article a ");
        StringBuilder where=new StringBuilder(" WHERE 1=1 ");
        addQuerryNomal(where,params);
        sql.append(where);
        Query query=entityManager.createNativeQuery(sql.toString(), Article.class);
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());
        List<Article> articles= query.getResultList();
        Long n=countArticleByRequest(params);
        return new PageImpl<>(articles, pageable,n);
    }


    public void addQuerryNomal(StringBuilder where,Map<String, Object> params) {
        for(String x:params.keySet()){
            if(NumberUtil.isNumber(params.get(x).toString())){
                where.append(" AND a."+x+" = '"+params.get(x).toString()+"' ");
            }else{
                where.append(" AND a."+x+" LIKE '%"+params.get(x)+"%' ");
            }
        }
    }

    public Long countArticleByRequest(Map<String, Object> params){
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT COUNT(*) FROM article a ");
        StringBuilder where=new StringBuilder(" WHERE 1=1 ");
        addQuerryNomal(where,params);
        sql.append(where);
        Query query=entityManager.createNativeQuery(sql.toString());
        return (Long) query.getSingleResult();
    }
}
