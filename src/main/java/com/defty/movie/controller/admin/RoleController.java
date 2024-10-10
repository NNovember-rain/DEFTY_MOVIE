package com.defty.movie.controller.admin;

import com.defty.movie.dto.request.RoleRequest;
import com.defty.movie.dto.response.ApiResponse;
import com.defty.movie.dto.response.RoleResponse;
import com.defty.movie.service.IRoleService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("${api.prefix}/admin/role")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleController {
    IRoleService roleService;

    @GetMapping("")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getRoles() {
        Set<RoleResponse> roleResponses = roleService.getAllRoles();
        ApiResponse<?> response = ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message("Roles retrieved successfully")
                .data(roleResponses)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createRole(@RequestBody RoleRequest roleRequest) {
        roleService.createRole(roleRequest);
        ApiResponse<?> response = ApiResponse.builder()
                .status(HttpStatus.CREATED.value())
                .message(HttpStatus.CREATED.getReasonPhrase())
                .data("Create Role")
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateRole(@PathVariable Integer id, @RequestBody RoleRequest roleRequest) {
        RoleResponse roleResponse = roleService.updateRole(id, roleRequest);
        ApiResponse<?> response = ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message("Role updated successfully")
                .data(roleResponse)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
