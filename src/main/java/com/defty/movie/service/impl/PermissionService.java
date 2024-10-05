package com.defty.movie.service.impl;

import com.defty.movie.dto.request.PermissionRequest;
import com.defty.movie.dto.response.PermissionResponse;
import com.defty.movie.dto.response.RoleResponse;
import com.defty.movie.entity.Permission;
import com.defty.movie.entity.Role;
import com.defty.movie.entity.RolePermission;
import com.defty.movie.mapper.PermissionMapper;
import com.defty.movie.repository.IPermissionRepository;
import com.defty.movie.repository.IRolePermissionRepository;
import com.defty.movie.repository.IRoleRepository;
import com.defty.movie.service.IPermissionService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
    @Transactional
    public void deletePermissions(List<Integer> permissionIds) {
        permissionRepository.deleteByIdIn(permissionIds);
    }

    @Override
    public RoleResponse getPermissionsByRoleId(Integer roleId) {
        Set<Permission> permissions = permissionRepository.findPermissionsByRoleId(roleId);
        Set<PermissionResponse> permissionResponses = permissions.stream().map(permissionMapper::toPermissionResponse).collect(Collectors.toSet());
        return RoleResponse.builder()
                .name(roleRepository.findById(roleId).get().getName())
                .description(roleRepository.findById(roleId).get().getDescription())
                .rolePermissions(permissionResponses)
                .build();
    }

    @Override
    public RoleResponse assignPermissionToRole(Integer roleId, List<Integer> permissionIds) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role not found with id: " + roleId));
        List<Permission> permissions = permissionRepository.findAllById(permissionIds);
        Set<Permission> existPermissions = permissionRepository.findPermissionsByRoleId(roleId);
        List<RolePermission> newRolePermissions = new ArrayList<>();
        for (Permission permission : permissions) {
            if (!existPermissions.contains(permission)) {
                RolePermission rolePermission = new RolePermission();
                rolePermission.setRole(role);
                rolePermission.setPermission(permission);
                newRolePermissions.add(rolePermission);
            }
        }
        rolePermissionRepository.saveAll(newRolePermissions);
        return RoleResponse.builder()
                .name(role.getName())
                .description(role.getDescription())
                .rolePermissions(role.getRolePermissions().stream()
                        .map(rolePermission -> permissionMapper.toPermissionResponse(rolePermission.getPermission()))
                        .collect(Collectors.toSet()))
                .build();
    }


    @Override
    public RoleResponse unassignPermissionFromRole(Integer roleId, List<Integer> permissionIds) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role not found with id: " + roleId));
        List<Permission> permissions = permissionRepository.findAllById(permissionIds);
        List<RolePermission> rolePermissions = new ArrayList<>();
        for (Permission permission : permissions) {
            RolePermission rolePermission = rolePermissionRepository.findByRoleIdAndPermissionId(roleId, permission.getId());
            rolePermissions.add(rolePermission);
        }
        rolePermissionRepository.deleteAll(rolePermissions);
        return RoleResponse.builder()
                .name(role.getName())
                .description(role.getDescription())
                .rolePermissions(role.getRolePermissions().stream()
                        .map(rolePermission -> permissionMapper.toPermissionResponse(rolePermission.getPermission()))
                        .collect(Collectors.toSet()))
                .build();
    }
}
