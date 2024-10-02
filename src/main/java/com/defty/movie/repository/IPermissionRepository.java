package com.defty.movie.repository;

import com.defty.movie.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IPermissionRepository extends JpaRepository<Permission, Integer> {
}
