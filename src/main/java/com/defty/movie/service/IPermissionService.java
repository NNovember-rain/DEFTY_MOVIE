package com.defty.movie.service;

import com.defty.movie.dto.request.PermissionRequest;
import com.defty.movie.dto.response.PermissionResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IPermissionService {
    PermissionResponse createPermission(PermissionRequest permissionRequest);
    PermissionResponse getPermissionById(Integer id);
    Page<PermissionResponse> getAllPermissions(String search, Pageable pageable);
    List<PermissionResponse> getAllPermissions();
    void deletePermissions(List<Integer> permissionIds);
    void updatePermission(Integer permissionId, PermissionRequest permissionRequest);
}
