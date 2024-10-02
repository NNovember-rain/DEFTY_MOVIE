package com.defty.movie.repository;

import com.defty.movie.entity.RolePermission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface IRolePermissionRepository extends JpaRepository<RolePermission, Integer> {
    Set<RolePermission> findByRoleId(Integer roleId);
}
