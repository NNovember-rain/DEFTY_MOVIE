package com.defty.movie.service;

import com.defty.movie.dto.request.RoleRequest;
import com.defty.movie.dto.response.RoleResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;

public interface IRoleService{
    Page<RoleResponse> getAllRoles(String name, Pageable pageable);
    RoleResponse createRole(RoleRequest roleRequest);
    RoleResponse updateRole(Integer id, RoleRequest roleRequest);
    RoleResponse getRoleId(Integer roleId);
    void deleteRole(List<Integer> roleId);
    RoleResponse assignPermissionToRole(Integer roleId, List<Integer> permissionIds);
    RoleResponse unassignPermissionFromRole(Integer roleId, List<Integer> permissionIds);
    Integer checkStatusRole(Integer roleId);
}
