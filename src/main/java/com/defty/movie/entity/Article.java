package com.defty.movie.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.Fetch;
import org.springframework.lang.NonNull;

import java.util.Date;

@Entity
@Getter
@Setter
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Column( length = 255)
    String content;

    @Column( length = 50)
    String author;

    @Column( length = 255)
    String thumbnail;

    @Column
    Date createdAt=new Date();

    @Column( length = 50)
    String slug;

    @Column
    Integer status=1;

    @ManyToOne
    @JoinColumn(name="account_id")
    Account account;

}
