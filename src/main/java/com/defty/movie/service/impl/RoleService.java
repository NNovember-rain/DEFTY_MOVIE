package com.defty.movie.service.impl;

import com.defty.movie.dto.request.RoleRequest;
import com.defty.movie.dto.response.PermissionResponse;
import com.defty.movie.dto.response.RoleResponse;
import com.defty.movie.entity.Permission;
import com.defty.movie.entity.Role;
import com.defty.movie.entity.RolePermission;
import com.defty.movie.exception.AlreadyExitException;
import com.defty.movie.exception.NotFoundException;
import com.defty.movie.mapper.PermissionMapper;
import com.defty.movie.repository.IPermissionRepository;
import com.defty.movie.repository.IRolePermissionRepository;
import com.defty.movie.repository.IRoleRepository;
import com.defty.movie.service.IRoleService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleService implements IRoleService {
    IRoleRepository roleRepository;
    IPermissionRepository permissionRepository;
    PermissionMapper permissionMapper;
    IRolePermissionRepository rolePermissionRepository;
    String PREFIX_ROLE = "ROLE | ";

    @Override
    public Page<RoleResponse> getAllRoles(String name, Pageable pageable) {
        Page<Role> roles;
        if (name != null && !name.isEmpty()) {
            roles = roleRepository.findRole(name, pageable);
            log.info("{}Get all roles by name: {}", PREFIX_ROLE, name);
        } else {
            roles = roleRepository.findAllWithStatus(pageable);
            log.info("{}Get all roles", PREFIX_ROLE);
        }
        return roles.map(role -> new RoleResponse(
                role.getId(),
                role.getName(),
                role.getDescription(),
                role.getStatus(),
                null
        ));
    }

    @Override
    public RoleResponse createRole(RoleRequest roleRequest) {
        if(roleRepository.findByName(roleRequest.getName()) != null) {
            log.error("{}Role already exist", PREFIX_ROLE);
            throw new AlreadyExitException("Role already exist");
        }
        Role role = Role.builder()
                .name(roleRequest.getName())
                .description(roleRequest.getDescription())
                .status(1)
                .build();
        roleRepository.save(role);
        return new RoleResponse(role.getId(), role.getName(), role.getDescription(), role.getStatus(), null);
    }

    @Override
    public RoleResponse getRoleId(Integer roleId) {
        Set<Permission> permissions = permissionRepository.findPermissionsByRoleId(roleId);
        log.info("{}Get permission by roleId: {}", PREFIX_ROLE, roleId);
        Set<PermissionResponse> permissionResponses = permissions.stream()
                .map(permissionMapper::toPermissionResponse)
                .collect(Collectors.toSet());
        log.info("{}Get role by id: {}", PREFIX_ROLE, roleId);
        Optional<Role> roleOptional = roleRepository.findById(roleId);
        if (roleOptional.isEmpty()) {
            log.error("{}Role not found roleId: {}", PREFIX_ROLE, roleId);
            throw new NotFoundException("Role not found with id: " + roleId);
        }
        Role role = roleOptional.get();
        return RoleResponse.builder()
                .name(role.getName())
                .description(role.getDescription())
                .rolePermissions(permissionResponses)
                .status(role.getStatus())
                .build();
    }

    @Override
    public Integer checkStatusRole(Integer roleId) {
        Optional<Role> roleOptional = roleRepository.findById(roleId);
        if(roleOptional.isEmpty()) {
            log.error("{}Role not found id: {}", PREFIX_ROLE, roleId);
            throw new NotFoundException("Role not found with id: " + roleId);
        }
        Role role = roleOptional.get();
        if (role.getStatus() == 1) {
            role.setStatus(0);
        }
        else if(role.getStatus() == 0) {
            role.setStatus(1);
        }
        roleRepository.save(role);
        return role.getStatus();
    }

    @Override
    public void deleteRole(List<Integer> roleId) {
        List<Role> roles = roleRepository.findAllById(roleId);
        log.info("{}Delete role by id: {}", PREFIX_ROLE, roleId);
        for (Role role : roles) {
            role.setStatus(-1);
            List<RolePermission> rolePermissions = rolePermissionRepository.findByRoleId(role.getId());
            rolePermissionRepository.deleteAll(rolePermissions);
            roleRepository.save(role);
        }
    }

    @Override
    public RoleResponse assignPermissionToRole(Integer roleId, List<Integer> permissionIds) {
        Optional<Role> roleOptional = roleRepository.findById(roleId);
        if (roleOptional.isEmpty()) {
            log.error("{}Role not found with roleId: {}", PREFIX_ROLE, roleId);
            throw new NotFoundException("Role not found with id: " + roleId);
        }
        Role role = roleOptional.get();
        if(role.getStatus() == 0){
            log.error("{}Role is disabled", PREFIX_ROLE);
            throw new NotFoundException("Role is disabled");
        }
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
        log.info("{}Assign permission to role: {}", PREFIX_ROLE, roleId);
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
        Optional<Role> roleOptional = roleRepository.findById(roleId);
        if (roleOptional.isEmpty()) {
            log.error("{}Role not found with id: {}", PREFIX_ROLE, roleId);
            throw new NotFoundException("Role not found with id: " + roleId);
        }
        Role role = roleOptional.get();
        if(role.getStatus() == 0){
            log.error("{}Role is disabled", PREFIX_ROLE);
            throw new NotFoundException("Role is disabled");
        }
        List<Permission> permissions = permissionRepository.findAllById(permissionIds);
        List<RolePermission> rolePermissions = new ArrayList<>();
        for (Permission permission : permissions) {
            RolePermission rolePermission = rolePermissionRepository.findByRoleIdAndPermissionId(roleId, permission.getId());
            rolePermissions.add(rolePermission);
        }
        rolePermissionRepository.deleteAll(rolePermissions);
        log.info("{}Unassign permission from role: {}", PREFIX_ROLE, roleId);
        return RoleResponse.builder()
                .name(role.getName())
                .description(role.getDescription())
                .rolePermissions(role.getRolePermissions().stream()
                        .map(rolePermission -> permissionMapper.toPermissionResponse(rolePermission.getPermission()))
                        .collect(Collectors.toSet()))
                .build();
    }

    @Override
    public RoleResponse updateRole(Integer roleId, RoleRequest roleRequest) {
        Optional<Role> roleOptional = roleRepository.findById(roleId);
        if (roleOptional.isEmpty()) {
            log.error("{}Role not found with id: {}", PREFIX_ROLE, roleId);
            throw new NotFoundException("Role not found with id: " + roleId);
        }
        Role role = roleOptional.get();
        role.setName(roleRequest.getName());
        role.setDescription(roleRequest.getDescription());
        roleRepository.save(role);
        log.info("{}Update role by id: {}", PREFIX_ROLE, roleId);
        return RoleResponse.builder()
                .name(roleRequest.getName())
                .description(roleRequest.getDescription())
                .build();
    }
}
