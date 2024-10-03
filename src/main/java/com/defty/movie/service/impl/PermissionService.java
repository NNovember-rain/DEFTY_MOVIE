package com.defty.movie.service.impl;

import com.defty.movie.dto.request.PermissionRequest;
import com.defty.movie.dto.response.PermissionResponse;
import com.defty.movie.dto.response.RoleResponse;
import com.defty.movie.entity.Permission;
import com.defty.movie.entity.RolePermission;
import com.defty.movie.mapper.PermissionMapper;
import com.defty.movie.repository.IPermissionRepository;
import com.defty.movie.repository.IRolePermissionRepository;
import com.defty.movie.repository.IRoleRepository;
import com.defty.movie.service.IPermissionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermissionService implements IPermissionService {
    IPermissionRepository permissionRepository;
    PermissionMapper permissionMapper;
    IRolePermissionRepository rolePermissionRepository;
    IRoleRepository roleRepository;

    @Override
    public PermissionResponse createPermission(PermissionRequest permissionRequest) {
        Permission permission = permissionMapper.toPermission(permissionRequest);
        permissionRepository.save(permission);
        return permissionMapper.toPermissionResponse(permission);
    }

    @Override
    public List<PermissionResponse> getAllPermissions() {
        List<Permission> permissions = permissionRepository.findAll();
        return permissions.stream().map(permissionMapper::toPermissionResponse).toList();
    }

    @Override
    public void deletePermissions(List<String> permissionIds) {

    }

    @Override
    public RoleResponse getPermissionsByRoleId(Integer roleId) {
        Set<RolePermission> rolePermissions = rolePermissionRepository.findByRoleId(roleId);
        Set<Permission> permissions = new HashSet<>();
        for (RolePermission rolePermission : rolePermissions) {
            Permission permission = permissionRepository.findById(rolePermission.getPermission().getId()).orElse(null);
            permissions.add(permission);
        }
        Set<PermissionResponse> permissionResponses = permissions.stream().map(permissionMapper::toPermissionResponse).collect(Collectors.toSet());
        return RoleResponse.builder()
                .name(roleRepository.findById(roleId).get().getName())
                .description(roleRepository.findById(roleId).get().getDescription())
                .rolePermissions(permissionResponses)
                .build();
    }

}
