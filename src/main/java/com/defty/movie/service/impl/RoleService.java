package com.defty.movie.service.impl;

import com.defty.movie.dto.request.RoleRequest;
import com.defty.movie.dto.response.RoleResponse;
import com.defty.movie.entity.Role;
import com.defty.movie.repository.IRoleRepository;
import com.defty.movie.service.IRoleService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleService implements IRoleService {
    IRoleRepository roleRepository;

    @Override
    public Set<RoleResponse> getAllRoles() {
        return roleRepository.findAll().stream()
                .map(role -> new RoleResponse(role.getName(), role.getDescription(), null))
                .collect(Collectors.toSet());
    }

    @Override
    public void createRole(RoleRequest roleRequest) {
        Role role = Role.builder()
                .name(roleRequest.getName())
                .description(roleRequest.getDescription())
                .build();
        roleRepository.save(role);
    }

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
