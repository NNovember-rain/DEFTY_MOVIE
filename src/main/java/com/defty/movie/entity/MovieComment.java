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
public class MovieComment extends BaseEntity {
    @Column
    String content;

    @Column
    int status;

    @ManyToOne
    @JoinColumn(name = "parent_movieComment_id")
    MovieComment parentMovieComment;

    @ManyToOne
    @JoinColumn(name = "episode_id")
    Episode episode;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;

    @OneToMany(mappedBy = "movieComment", fetch = FetchType.LAZY)
    Set<MovieCommentReaction> movieCommentReactions;
}
