package com.defty.movie.repository;

import com.defty.movie.entity.DirectorEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IDirectorRepository extends JpaRepository<DirectorEntity, Integer> {
}
