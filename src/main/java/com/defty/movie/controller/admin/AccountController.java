package com.defty.movie.controller.admin;

import com.defty.movie.dto.request.LoginRequest;
import com.defty.movie.dto.response.AccountResponse;
import com.defty.movie.dto.response.ApiResponse;
import com.defty.movie.dto.response.LoginResponse;
import com.defty.movie.service.IAccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/admin/account")
public class AccountController {
    private final IAccountService accountService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest){
        String token = accountService.login(loginRequest);
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setToken(token);
        ApiResponse<LoginResponse> response = ApiResponse.<LoginResponse>builder()
                .status(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .data(loginResponse)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/check-account")
    public ResponseEntity<?> checkAccount(@RequestHeader(value = "Authorization", required = false) String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            ApiResponse<?> response = ApiResponse.builder()
                    .status(HttpStatus.UNAUTHORIZED.value())
                    .message("Not logged in or do not have a valid token")
                    .data(null)
                    .build();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
        String extractedToken = authorizationHeader.substring(7);
        AccountResponse accountResponse = accountService.getAccountFromToken(extractedToken);
        ApiResponse<?> response = ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .data(accountResponse)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
