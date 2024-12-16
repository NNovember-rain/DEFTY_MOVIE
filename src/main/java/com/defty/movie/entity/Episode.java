package com.defty.movie.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "episode")
public class Episode extends BaseEntity{

    @Column(name = "number")
    private Integer number;

    @Column(name = "description")
    private String description;

    @Column(name = "thumbnail")
    private String thumbnail;

    @Column(name = "link")
    private String link;

    @Column(name = "status")
    private Integer status;

    @ManyToOne
    @JoinColumn(name = "movie_id", nullable = false)
    private Movie movie;

    @OneToMany(mappedBy = "episode", fetch = FetchType.LAZY)
    private Set<WatchedHistory> watchedHistory;

    @OneToMany(mappedBy = "episode", fetch = FetchType.LAZY)
    private Set<EpisodeAccount> episodeAccounts;

    @OneToMany(mappedBy = "episode", fetch = FetchType.LAZY)
    private Set<MovieComment> movieComments;
}
