package com.defty.movie.entity;
import jakarta.persistence.*;
import lombok.Data;
@Data
@Entity
@Table(name = "episode")
public class EpisodeEntity extends BaseEntity{

    @Column(name = "number")
    private Integer number;

    @Column(name = "description")
    private String description;

    @Column(name = "thumbnail")
    private String thumbnail;

    @Column(name = "link")
    private String link;

    @ManyToOne
    @JoinColumn(name = "movie_id", nullable = false)
    private MovieEntity movie;
}
