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
@RequestMapping("${api.prefix}/admin/permission")
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermissionController {
    IPermissionService permissionService;

    @GetMapping("/all")
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

    @PostMapping("")
    @PreAuthorize("@requiredPermission.checkPermission('CREATE_PERMISSION')")
    public ResponseEntity<?> createPermission(@RequestBody PermissionRequest permissionRequest) {
        PermissionResponse permissionResponse = permissionService.createPermission(permissionRequest);
        ApiResponse<?> response = ApiResponse.builder()
                .status(HttpStatus.CREATED.value())
                .message(HttpStatus.CREATED.getReasonPhrase())
                .data(permissionResponse.getId())
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PatchMapping("/{permissionId}")
    @PreAuthorize("@requiredPermission.checkPermission('UPDATE_PERMISSION')")
    public ResponseEntity<?> updatePermission(@PathVariable("permissionId") Integer permissionId, @RequestBody PermissionRequest permissionRequest) {
        permissionService.updatePermission(permissionId, permissionRequest);
        ApiResponse<?> response = ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .data("success")
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{permissionIds}")
    @PreAuthorize("@requiredPermission.checkPermission('DELETE_PERMISSION')")
    public ResponseEntity<?> unassignPermission(@PathVariable List<Integer> permissionIds) {
        permissionService.deletePermissions(permissionIds);
        return ResponseEntity.ok().build();
    }

}
