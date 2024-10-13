package com.defty.movie.service;

import com.defty.movie.dto.request.PermissionRequest;
import com.defty.movie.dto.response.PermissionResponse;

import java.util.List;

public interface IPermissionService {
    PermissionResponse createPermission(PermissionRequest permissionRequest);
    List<PermissionResponse> getAllPermissions();
    void deletePermissions(List<Integer> permissionIds);
    void updatePermission(Integer permissionId, PermissionRequest permissionRequest);
}
