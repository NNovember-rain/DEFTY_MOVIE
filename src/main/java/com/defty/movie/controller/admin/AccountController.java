package com.defty.movie.controller.admin;

import com.defty.movie.dto.request.LoginRequest;
import com.defty.movie.dto.request.RefreshTokenRequest;
import com.defty.movie.dto.response.AccountResponse;
import com.defty.movie.dto.response.ApiResponse;
import com.defty.movie.dto.response.LoginResponse;
import com.defty.movie.dto.response.RefreshTokenResponse;
import com.defty.movie.security.JwtTokenUtil;
import com.defty.movie.service.IAccountService;
import com.defty.movie.service.IRefreshTokenService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/admin/account")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AccountController {
    IAccountService accountService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest){
        LoginResponse loginResponse = accountService.login(loginRequest);
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
        try {
            AccountResponse accountResponse = accountService.getAccountFromToken(extractedToken);
            ApiResponse<?> response = ApiResponse.builder()
                    .status(HttpStatus.OK.value())
                    .message(HttpStatus.OK.getReasonPhrase())
                    .data(accountResponse)
                    .build();
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            ApiResponse<?> response = ApiResponse.builder()
                    .status(HttpStatus.UNAUTHORIZED.value())
                    .message("Token is expired")
                    .data(null)
                    .build();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequest request) {
       RefreshTokenResponse newToken = accountService.refreshToken(request.getRefreshToken());
       ApiResponse<?> response = ApiResponse.builder()
               .status(HttpStatus.OK.value())
               .message(HttpStatus.OK.getReasonPhrase())
               .data(newToken)
               .build();
       return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader(value = "Authorization", required = false) String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Access token is missing or invalid");
        }
        String accessToken = authorizationHeader.substring(7);
        accountService.logout(accessToken);
        ApiResponse<?> response = ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .data("Account logged out successfully")
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
