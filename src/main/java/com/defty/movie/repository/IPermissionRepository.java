package com.defty.movie.repository;

import com.defty.movie.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IPermissionRepository extends JpaRepository<Permission, Integer> {
    void deleteByIdIn(List<Integer> permissionIds);
}
