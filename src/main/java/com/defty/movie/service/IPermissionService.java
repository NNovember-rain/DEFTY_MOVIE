package com.defty.movie.service;

import com.defty.movie.dto.request.PermissionRequest;
import com.defty.movie.dto.response.PermissionResponse;
import com.defty.movie.dto.response.RoleResponse;

import java.util.List;

public interface IPermissionService {
    PermissionResponse createPermission(PermissionRequest permissionRequest);
    List<PermissionResponse> getAllPermissions();
    void deletePermissions(List<Integer> permissionIds);
    RoleResponse getPermissionsByRoleId(Integer roleId);
    RoleResponse assignPermissionToRole(Integer roleId, List<Integer> permissionIds);
    RoleResponse unassignPermissionFromRole(Integer roleId, List<Integer> permissionIds);
}
