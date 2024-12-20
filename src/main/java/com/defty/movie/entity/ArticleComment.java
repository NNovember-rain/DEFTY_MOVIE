package com.defty.movie.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Entity
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ArticleComment extends BaseEntity {
    @Column
    String content;

    @Column
    int status;

    @ManyToOne
    @JoinColumn(name = "parent_articleComment_id")
    ArticleComment parentArticleComment;

    @ManyToOne
    @JoinColumn(name = "article_id")
    Article article;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;

    @OneToMany(mappedBy = "articleComment", fetch = FetchType.LAZY)
    Set<ArticleCommentReaction> articleCommentReactions;
}
