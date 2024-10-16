package com.defty.movie.repository;

import com.defty.movie.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IRoleRepository extends JpaRepository<Role,Integer> {
    List<Role> findByStatus(Integer status);
}
