package com.defty.movie.controller.admin;

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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
//    @PreAuthorize("ha")
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
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getPermissionByRoleId(@PathVariable("roleId") Integer roleId) {
        RoleResponse roleResponse = permissionService.getPermissionsByRoleId(roleId);
        ApiResponse<?> response = ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .data(roleResponse)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
