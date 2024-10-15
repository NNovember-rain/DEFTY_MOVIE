package com.defty.movie.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.Set;

@Data
@Entity
@Table(name = "movie")
public class MovieEntity extends BaseEntity{
    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "trailer")
    private String trailer;

    @Column(name = "thubnail")
    private String thubnail;

    @Column(name = "cover_image")
    private String coverImage;

    @Column(name = "status")
    private Integer status = 1;

    @Column(name = "nation")
    private String nation;

    @Column(name = "release_date")
    private Date releaseDate;

    @Column(name = "ranking")
    private Integer ranking;

    @Column(name = "slug")
    private String slug;

    @Column(name = "membership_type")
    private Integer membershipType;

    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL)
    private Set<MovieCategoryEntity> movieCategories;

    @ManyToOne
    @JoinColumn(name = "director_id", nullable = false)
    private DirectorEntity director;

    @ManyToMany
    @JoinTable(
            name = "actor_movie",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "actor_id")
    )
    private Set<ActorEntity> actors;

    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<EpisodeEntity> episodes;
}
