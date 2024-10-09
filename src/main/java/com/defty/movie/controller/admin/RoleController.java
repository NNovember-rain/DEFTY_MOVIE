package com.defty.movie.controller.admin;

import com.defty.movie.dto.request.RoleRequest;
import com.defty.movie.dto.response.ApiResponse;
import com.defty.movie.dto.response.RoleResponse;
import com.defty.movie.service.IRoleService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("${api.prefix}/admin")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleController {
    IRoleService roleService;

    @GetMapping("/roles")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<?> getRoles() {
        Set<RoleResponse> roleResponses = roleService.getAllRoles();
        return ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message("Role created successfully")
                .data(roleResponses)
                .build();
    }

    @PostMapping("/role")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<?> createRole(@RequestBody RoleRequest roleRequest) {
        roleService.createRole(roleRequest);
        return ApiResponse.builder()
                .status(HttpStatus.CREATED.value())
                .message(HttpStatus.CREATED.getReasonPhrase())
                .data("Create Role")
                .build();
    }

    @PutMapping("/role/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<?> updateRole(@PathVariable Integer id, @RequestBody RoleRequest roleRequest) {
        RoleResponse roleResponse = roleService.updateRole(id, roleRequest);
        return ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message("Role updated successfully")
                .data(roleResponse)
                .build();
    }
}
