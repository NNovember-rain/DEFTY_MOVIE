package com.defty.movie.repository;

import com.defty.movie.entity.ActorEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IActorRepository extends JpaRepository<ActorEntity, Integer> {
}
