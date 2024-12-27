package com.defty.movie.repository;

import com.defty.movie.entity.RolePermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IRolePermissionRepository extends JpaRepository<RolePermission, Integer> {
    RolePermission findByRoleIdAndPermissionId(Integer roleId, Integer permissionId);
    List<RolePermission> findByRoleId(Integer roleId);
}
