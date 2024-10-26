package com.defty.movie.repository;

import com.defty.movie.entity.Actor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IActorRepository extends JpaRepository<Actor, Integer> {
}
