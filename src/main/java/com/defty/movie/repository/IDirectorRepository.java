package com.defty.movie.repository;

import com.defty.movie.entity.Director;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IDirectorRepository extends JpaRepository<Director, Integer> {
}
