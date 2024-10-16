package com.defty.movie.repository;

import com.defty.movie.entity.Permission;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface IPermissionRepository extends JpaRepository<Permission, Integer> {
    @Query("SELECT p FROM Permission p " +
            "JOIN RolePermission rp ON p.id = rp.permission.id " +
            "WHERE rp.role.id = :roleId")
    Set<Permission> findPermissionsByRoleId(@Param("roleId") Integer roleId);

    @Query("SELECT p FROM Permission p " +
            "WHERE LOWER(p.name) " +
            "LIKE LOWER(CONCAT('%', :name, '%'))")
    Page<Permission> findPermission(@Param("name") String name, Pageable pageable);

    void deleteByIdIn(List<Integer> permissionIds);
}
