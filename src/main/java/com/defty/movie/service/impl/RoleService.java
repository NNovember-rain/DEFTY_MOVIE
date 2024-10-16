package com.defty.movie.service.impl;

import com.defty.movie.dto.request.RoleRequest;
import com.defty.movie.dto.response.PermissionResponse;
import com.defty.movie.dto.response.RoleResponse;
import com.defty.movie.entity.Permission;
import com.defty.movie.entity.Role;
import com.defty.movie.entity.RolePermission;
import com.defty.movie.mapper.PermissionMapper;
import com.defty.movie.repository.IPermissionRepository;
import com.defty.movie.repository.IRolePermissionRepository;
import com.defty.movie.repository.IRoleRepository;
import com.defty.movie.service.IRoleService;
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
public class RoleService implements IRoleService {
    IRoleRepository roleRepository;
    IPermissionRepository permissionRepository;
    PermissionMapper permissionMapper;
    IRolePermissionRepository rolePermissionRepository;

    @Override
    public Set<RoleResponse> getAllRoles() {
        return roleRepository.findByStatus(1).stream()
                .map(role -> new RoleResponse(role.getId(), role.getName(), role.getDescription(), null))
                .collect(Collectors.toSet());
    }

    @Override
    public RoleResponse createRole(RoleRequest roleRequest) {
        Role role = Role.builder()
                .name(roleRequest.getName())
                .description(roleRequest.getDescription())
                .build();
        roleRepository.save(role);
        return new RoleResponse(role.getId(), role.getName(), role.getDescription(), null);
    }

    @Override
    public RoleResponse getRoleId(Integer roleId) {
        Set<Permission> permissions = permissionRepository.findPermissionsByRoleId(roleId);
        Set<PermissionResponse> permissionResponses = permissions.stream().map(permissionMapper::toPermissionResponse).collect(Collectors.toSet());
        return RoleResponse.builder()
                .name(roleRepository.findById(roleId).get().getName())
                .description(roleRepository.findById(roleId).get().getDescription())
                .rolePermissions(permissionResponses)
                .build();
    }

    @Override
    public void deleteRole(Integer roleId) {
        Role role = roleRepository.findById(roleId).orElseThrow(
                () -> new RuntimeException("Role not found")
        );
        role.setStatus(0);
        roleRepository.save(role);
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

    @Override
    public RoleResponse updateRole(Integer id, RoleRequest roleRequest) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Role not found"));
        role.setName(roleRequest.getName());
        role.setDescription(roleRequest.getDescription());
        roleRepository.save(role);
        return RoleResponse.builder()
                .name(roleRequest.getName())
                .description(roleRequest.getDescription())
                .build();
    }

}
