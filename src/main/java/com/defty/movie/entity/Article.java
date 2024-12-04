package com.defty.movie.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Entity
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Article extends BaseEntity{

    @Column
    @NonNull
    String title;

    @Column( length = 255)
    String content;

    @Column( length = 50)
    String author;

    @Column( length = 255)
    String thumbnail;

    @Column( length = 50)
    String slug;

    @Column
    Integer status=1;

    @ManyToOne
    @JoinColumn(name="account_id")
    Account account;

    @OneToMany(mappedBy = "article", fetch = FetchType.LAZY)
    Set<ArticleComment> articleComments;
}
