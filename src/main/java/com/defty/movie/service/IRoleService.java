package com.defty.movie.service;

import com.defty.movie.dto.request.RoleRequest;
import com.defty.movie.dto.response.RoleResponse;

import java.util.List;
import java.util.Set;

public interface IRoleService{
    Set<RoleResponse> getAllRoles();
    RoleResponse createRole(RoleRequest roleRequest);
    RoleResponse updateRole(Integer id, RoleRequest roleRequest);
    RoleResponse getRoleId(Integer roleId);
    RoleResponse assignPermissionToRole(Integer roleId, List<Integer> permissionIds);
    RoleResponse unassignPermissionFromRole(Integer roleId, List<Integer> permissionIds);
}
