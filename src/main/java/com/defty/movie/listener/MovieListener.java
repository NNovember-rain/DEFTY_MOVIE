package com.defty.movie.listener;

import com.defty.movie.entity.Movie;
import com.defty.movie.service.IMovieUserRedisService;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PreRemove;
import jakarta.persistence.PreUpdate;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MovieListener {
    IMovieUserRedisService movieHomeUserRedisService;

    @PostPersist //save = persis
    public void postPersist(Movie movie) {
        movieHomeUserRedisService.clearCache();
    }

    @PreUpdate
    public void postUpdate(Movie movie) {
        movieHomeUserRedisService.clearCache();
    }

    @PreRemove
    public void preRemove(Movie movie) {
        movieHomeUserRedisService.clearCache();

    }
}
