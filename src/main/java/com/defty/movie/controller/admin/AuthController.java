package com.defty.movie.controller.admin;

import com.defty.movie.dto.request.LoginRequest;
import com.defty.movie.dto.response.AccountResponse;
import com.defty.movie.dto.response.ApiResponse;
import com.defty.movie.dto.response.LoginResponse;
import com.defty.movie.dto.response.RefreshTokenResponse;
import com.defty.movie.service.IAuthService;
import com.defty.movie.utils.CookieUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
@RequestMapping("${api.prefix}/admin/auth")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthController {
    String PREFIX_AUTH = "AUTH | ";
    IAuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, HttpServletResponse res){
        LoginResponse loginResponse = authService.login(loginRequest, res);
        log.info(PREFIX_AUTH + "Login success");
        ApiResponse<LoginResponse> response = ApiResponse.<LoginResponse>builder()
                .status(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .data(loginResponse)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/check-account")
    public ResponseEntity<?> checkAccount(HttpServletRequest request) {
        String token = CookieUtil.getValue(request, "access_token");
        if (token == null || token.isEmpty()) {
            ApiResponse<?> response = ApiResponse.builder()
                    .status(HttpStatus.UNAUTHORIZED.value())
                    .message("Not logged in or do not have a valid token")
                    .data("Ok")
                    .data(null)
                    .build();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
        try {
            AccountResponse accountResponse = authService.getAccountFromToken(token);
            log.info(PREFIX_AUTH + "Check account success");
            ApiResponse<?> response = ApiResponse.builder()
                    .status(HttpStatus.OK.value())
                    .message(HttpStatus.OK.getReasonPhrase())
                    .data(accountResponse)
                    .build();
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            log.error(PREFIX_AUTH + "Token is expired or invalid");
            ApiResponse<?> response = ApiResponse.builder()
                    .status(HttpStatus.UNAUTHORIZED.value())
                    .message("Token is expired or invalid")
                    .data(null)
                    .build();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(HttpServletRequest request, HttpServletResponse res) {
        String refreshToken = CookieUtil.getValue(request, "refresh_token");
        log.info(PREFIX_AUTH + "Refresh token: {}", refreshToken);
        RefreshTokenResponse newToken = authService.refreshToken(refreshToken, res);
        log.info(PREFIX_AUTH + "Refresh token success");
        ApiResponse<?> response = ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .data(newToken)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        String accessToken = CookieUtil.getValue(request, "access_token");
        authService.logout(accessToken);
        CookieUtil.clear(response, "access_token");
        CookieUtil.clear(response, "refresh_token");
        log.info(PREFIX_AUTH + "Logout success");
        ApiResponse<?> responseObj = ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .data(null)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(responseObj);
    }
}
