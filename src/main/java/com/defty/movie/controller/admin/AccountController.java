package com.defty.movie.controller.admin;

import com.defty.movie.dto.request.LoginRequest;
import com.defty.movie.dto.response.ApiResponse;
import com.defty.movie.dto.response.LoginResponse;
import com.defty.movie.common.ApiStatus;
import com.defty.movie.service.IAccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/account")
public class AccountController {
    private final IAccountService accountService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@RequestBody LoginRequest loginRequest) {
        try {
            String token = accountService.login(loginRequest);
            LoginResponse loginResponse = new LoginResponse();
            loginResponse.setToken(token);
            ApiResponse<LoginResponse> response = ApiResponse.<LoginResponse>builder()
                    .status(ApiStatus.SUCCESS.getCode())
                    .message(ApiStatus.SUCCESS. getMessage())
                    .data(loginResponse)
                    .build();
            return ResponseEntity.status(HttpStatus.OK).body(response);

        } catch (Exception e) {
            log.error("Login failed: {}", e.getMessage(), e);
            ApiResponse<LoginResponse> response = ApiResponse.<LoginResponse>builder()
                    .status(ApiStatus.UNAUTHORIZED.getCode())
                    .message("Unauthorized: Invalid username or password")
                    .build();

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }
}
