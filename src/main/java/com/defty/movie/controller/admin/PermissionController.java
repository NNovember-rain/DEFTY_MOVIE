package com.defty.movie.controller.admin;

import com.defty.movie.dto.request.PermissionRequest;
import com.defty.movie.dto.response.ApiResponse;
import com.defty.movie.dto.response.PermissionResponse;
import com.defty.movie.service.IPermissionService;
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
@RequestMapping("${api.prefix}/admin/permission")
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermissionController {
    IPermissionService permissionService;

    @GetMapping("")
    @PreAuthorize("@requiredPermission.checkPermission('GET_ALL_PERMISSIONS')")
    public ResponseEntity<?> getPermissions(@RequestParam(value = "page", required = false) Integer page,
                                            @RequestParam(value = "size", required = false) Integer size,
                                            @RequestParam(value = "name", required = false) String name) {
        ApiResponse<?> response;
        if (page == null || size == null) {
            List<PermissionResponse> permissionResponses = permissionService.getAllPermissions();
            response = ApiResponse.builder()
                    .status(HttpStatus.OK.value())
                    .message(HttpStatus.OK.getReasonPhrase())
                    .data(permissionResponses)
                    .build();
        } else {
            Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
            Page<PermissionResponse> permissionResponses = permissionService.getAllPermissions(name, pageable);
            response = ApiResponse.builder()
                    .status(HttpStatus.OK.value())
                    .message(HttpStatus.OK.getReasonPhrase())
                    .data(permissionResponses)
                    .build();
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @PreAuthorize("@requiredPermission.checkPermission('GET_PERMISSION')")
    public ResponseEntity<?> getPermissionById(@PathVariable Integer id) {
        PermissionResponse permissionResponse = permissionService.getPermissionById(id);
        ApiResponse<?> response = ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .data(permissionResponse)
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
    public ResponseEntity<?> updatePermission(@PathVariable("permissionId") Integer permissionId,
                                              @RequestBody PermissionRequest permissionRequest) {
        permissionService.updatePermission(permissionId, permissionRequest);
        ApiResponse<?> response = ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .data("success")
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{permissionIds}")
    @PreAuthorize("@requiredPermission.checkPermission('DELETE_PERMISSIONS')")
    public ResponseEntity<?> unassignPermission(@PathVariable List<Integer> permissionIds) {
        permissionService.deletePermissions(permissionIds);
        ApiResponse<?> response = ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .data("success")
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
