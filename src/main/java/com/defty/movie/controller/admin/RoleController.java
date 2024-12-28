package com.defty.movie.controller.admin;

import com.defty.movie.dto.request.RoleRequest;
import com.defty.movie.dto.response.ApiResponse;
import com.defty.movie.dto.response.RoleResponse;
import com.defty.movie.service.IRoleService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("${api.prefix}/admin/role")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleController {
    String PREFIX_ROLE = "ROLE | ";
    IRoleService roleService;

    @GetMapping("")
    @PreAuthorize("@requiredPermission.checkPermission('GET_ALL_ROLES')")
    public ResponseEntity<?> getAllRoles(@RequestParam(value = "page", defaultValue = "0") int page,
                                         @RequestParam(value = "size", defaultValue = "10") int size,
                                         @RequestParam(value = "name", required = false) String name) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<RoleResponse> roleResponses = roleService.getAllRoles(name, pageable);
        log.info(PREFIX_ROLE + "Get all roles success");
        ApiResponse<?> response = ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .data(roleResponses)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{roleId}")
    @PreAuthorize("@requiredPermission.checkPermission('GET_ROLE')")
    public ResponseEntity<?> getRoleId(@PathVariable("roleId") Integer roleId) {
        RoleResponse roleResponse = roleService.getRoleId(roleId);
        log.info(PREFIX_ROLE + "Get role by id success");
        ApiResponse<?> response = ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .data(roleResponse)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/status/{id}")
    @PreAuthorize("@requiredPermission.checkPermission('STATUS_ROLE')")
    public ResponseEntity<?> switchStatus(@PathVariable("id") Integer roleId) {
        Integer status = roleService.checkStatusRole(roleId);
        log.info(PREFIX_ROLE + "Switch status role success");
        ApiResponse<?> response = ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .data(status)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("")
    @PreAuthorize("@requiredPermission.checkPermission('CREATE_ROLE')")
    public ResponseEntity<?> createRole(@RequestBody RoleRequest roleRequest) {
        RoleResponse roleResponse = roleService.createRole(roleRequest);
        log.info(PREFIX_ROLE + "Create role success");
        ApiResponse<?> response = ApiResponse.builder()
                .status(HttpStatus.CREATED.value())
                .message(HttpStatus.CREATED.getReasonPhrase())
                .data(roleResponse.getId())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{ids}")
    @PreAuthorize("@requiredPermission.checkPermission('DELETE_ROLES')")
    public ResponseEntity<?> deleteRole(@PathVariable("ids") List<Integer> ids) {
        roleService.deleteRole(ids);
        log.info(PREFIX_ROLE + "Delete role success");
        ApiResponse<?> response = ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .data("success")
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("@requiredPermission.checkPermission('UPDATE_ROLE')")
    public ResponseEntity<?> updateRole(@PathVariable Integer id,
                                        @RequestBody RoleRequest roleRequest) {
        RoleResponse roleResponse = roleService.updateRole(id, roleRequest);
        log.info(PREFIX_ROLE + "Update role success");
        ApiResponse<?> response = ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .data(roleResponse)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PatchMapping("/assignment/{permissionIds}")
    @PreAuthorize("@requiredPermission.checkPermission('ASSIGN_PERMISSION_TO_ROLE')")
    public ResponseEntity<?> assignPermissions(@RequestParam Integer roleId,
                                               @PathVariable List<Integer> permissionIds) {
        RoleResponse roleResponse = roleService.assignPermissionToRole(roleId, permissionIds);
        log.info(PREFIX_ROLE + "Assign permission to role success");
        ApiResponse<?> response = ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .data(roleResponse)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/unassignment/{permissionIds}")
    @PreAuthorize("@requiredPermission.checkPermission('UNASSIGN_PERMISSION_FROM_ROLE')")
    public ResponseEntity<?> unassignPermissions(@RequestParam Integer roleId,
                                                 @PathVariable List<Integer> permissionIds) {
        RoleResponse roleResponse = roleService.unassignPermissionFromRole(roleId, permissionIds);
        log.info(PREFIX_ROLE + "Unassign permission from role success");
        ApiResponse<?> response = ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .data(roleResponse)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
