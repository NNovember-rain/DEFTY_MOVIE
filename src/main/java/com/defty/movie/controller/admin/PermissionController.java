package com.defty.movie.controller.admin;

import com.defty.movie.dto.request.PermissionRequest;
import com.defty.movie.dto.response.ApiResponse;
import com.defty.movie.dto.response.PermissionResponse;
import com.defty.movie.dto.response.RoleResponse;
import com.defty.movie.service.IPermissionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/admin/permissions")
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermissionController {
    IPermissionService permissionService;

    @GetMapping("/get-all-permissions")
    @PreAuthorize("hasRole('ADMIN')")
//    @PreAuthorize("hasPermission()")
    public ResponseEntity<?> getALlPermissions() {
        List<PermissionResponse> permissionResponses = permissionService.getAllPermissions();
        ApiResponse<?> response = ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .data(permissionResponses)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/get-permissions/{roleId}")
    @PreAuthorize("requiredPermission.checkPermission('GET_PERMISSION')")
    public ResponseEntity<?> getPermissionByRoleId(@PathVariable("roleId") Integer roleId) {
        RoleResponse roleResponse = permissionService.getPermissionsByRoleId(roleId);
        ApiResponse<?> response = ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .data(roleResponse)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/create-permission")
    public ResponseEntity<?> assignPermission(@RequestBody PermissionRequest permissionRequest) {
        PermissionResponse permissionResponse = permissionService.createPermission(permissionRequest);
        ApiResponse<?> response = ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .data(permissionResponse)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/delete-permission/{permissionIds}")
    public ResponseEntity<?> unassignPermission(@PathVariable List<String> permissionIds) {
        PermissionResponse permissionResponse
    }
}
