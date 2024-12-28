package com.defty.movie.service.impl;

import com.defty.movie.dto.request.PermissionRequest;
import com.defty.movie.dto.response.PermissionResponse;
import com.defty.movie.entity.Permission;
import com.defty.movie.exception.AlreadyExitException;
import com.defty.movie.mapper.PermissionMapper;
import com.defty.movie.repository.IPermissionRepository;
import com.defty.movie.service.IPermissionService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermissionService implements IPermissionService {
    String PREFIX_PERMISSION = "PERMISSION | ";
    IPermissionRepository permissionRepository;
    PermissionMapper permissionMapper;

    @Override
    public PermissionResponse createPermission(PermissionRequest permissionRequest) {
        if(permissionRepository.findByName(permissionRequest.getName()) != null){
            log.error("{}Permission already exist", PREFIX_PERMISSION);
            throw new AlreadyExitException("Permission already exist");
        }
        Permission permission = permissionMapper.toPermission(permissionRequest);
        permissionRepository.save(permission);
        return permissionMapper.toPermissionResponse(permission);
    }

    @Override
    public PermissionResponse getPermissionById(Integer id) {
        Optional<Permission> permissionOptional = permissionRepository.findById(id);
        if(permissionOptional.isEmpty()){
            log.error("{}Permission not found", PREFIX_PERMISSION);
            throw new RuntimeException("Permission not found");
        }
        Permission permission = permissionOptional.get();
        log.info("{}Get permission by id: {}", PREFIX_PERMISSION, id);
        return permissionMapper.toPermissionResponse(permission);
    }

    @Override
    public Page<PermissionResponse> getAllPermissions(String name, Pageable pageable) {
        Page<Permission> permissions;
        if (name != null && !name.isEmpty()) {
            permissions = permissionRepository.findPermission(name, pageable);
            log.info("{}Get all permissions by name: {}", PREFIX_PERMISSION, name);
        } else {
            permissions = permissionRepository.findAll(pageable);
            log.info("{}Get all permissions with pageable", PREFIX_PERMISSION);
        }
        return permissions.map(permissionMapper::toPermissionResponse);
    }

    @Override
    public List<PermissionResponse> getAllPermissions() {
        List<Permission> permissions = permissionRepository.findAll();
        log.info("{}Get all permissions", PREFIX_PERMISSION);
        return permissions.stream().
                map(permissionMapper::toPermissionResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deletePermissions(List<Integer> permissionIds) {
        permissionRepository.deleteByIdIn(permissionIds);
        log.info("{}Delete permissions by ids: {}", PREFIX_PERMISSION, permissionIds);
    }

    @Override
    public void updatePermission(Integer permissionId, PermissionRequest permissionRequest) {
        Optional<Permission> permissionOptional = permissionRepository.findById(permissionId);
        if(permissionOptional.isEmpty()){
            log.error("{}Permission not found", PREFIX_PERMISSION);
            throw new RuntimeException("Permission not found");
        }
        Permission permission = permissionOptional.get();
        permission.setName(permissionRequest.getName());
        permission.setDescription(permissionRequest.getDescription());
        permissionRepository.save(permission);
        log.info("{}Update permission by id: {}", PREFIX_PERMISSION, permissionId);
    }
}
