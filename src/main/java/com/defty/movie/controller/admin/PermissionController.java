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
@RequestMapping("${api.prefix}/admin")
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermissionController {
    IPermissionService permissionService;

    @GetMapping("/permissions")
    @PreAuthorize("@requiredPermission.checkPermission('GET_ALL_PERMISSIONS')")
    public ResponseEntity<?> getALlPermissions() {
        List<PermissionResponse> permissionResponses = permissionService.getAllPermissions();
        ApiResponse<?> response = ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .data(permissionResponses)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/permissions/{roleId}")
    @PreAuthorize("@requiredPermission.checkPermission('GET_PERMISSIONS_ROLE')")
    public ResponseEntity<?> getPermissionByRoleId(@PathVariable("roleId") Integer roleId) {
        RoleResponse roleResponse = permissionService.getPermissionsByRoleId(roleId);
        ApiResponse<?> response = ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .data(roleResponse)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/permission")
    @PreAuthorize("@requiredPermission.checkPermission('CREATE_PERMISSION')")
    public ResponseEntity<?> createPermission(@RequestBody PermissionRequest permissionRequest) {
        PermissionResponse permissionResponse = permissionService.createPermission(permissionRequest);
        ApiResponse<?> response = ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .data(permissionResponse)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/permission/{permissionIds}")
    @PreAuthorize("@requiredPermission.checkPermission('DELETE_PERMISSION')")
    public ResponseEntity<?> unassignPermission(@PathVariable List<Integer> permissionIds) {
        permissionService.deletePermissions(permissionIds);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/assign-permissions/{permissionIds}")
    @PreAuthorize("@requiredPermission.checkPermission('ASSIGN_PERMISSION')")
    public ResponseEntity<?> assignPermissions(@RequestParam Integer roleId, @PathVariable List<Integer> permissionIds) {
        RoleResponse roleResponse = permissionService.assignPermissionToRole(roleId, permissionIds);
        ApiResponse<?> response = ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .data(roleResponse)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/unassign-permissions/{permissionIds}")
    @PreAuthorize("@requiredPermission.checkPermission('UNASSIGN_PERMISSION')")
    public ResponseEntity<?> unassignPermissions(@RequestParam Integer roleId, @PathVariable List<Integer> permissionIds) {
        RoleResponse roleResponse = permissionService.unassignPermissionFromRole(roleId, permissionIds);
        ApiResponse<?> response = ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .data(roleResponse)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
