package com.defty.movie.service;

import com.defty.movie.dto.request.PermissionRequest;
import com.defty.movie.dto.response.PermissionResponse;
import com.defty.movie.dto.response.RoleResponse;

import java.util.List;
import java.util.Set;

public interface IPermissionService {
    PermissionResponse createPermission(PermissionRequest permissionRequest);
    List<PermissionResponse> getAllPermissions();
    void deletePermissions(List<String> permissionIds);
    RoleResponse getPermissionsByRoleId(Integer roleId);
}
