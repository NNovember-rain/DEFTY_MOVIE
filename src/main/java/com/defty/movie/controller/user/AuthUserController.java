package com.defty.movie.controller.user;

import com.defty.movie.dto.request.LoginRequest;
import com.defty.movie.dto.request.RegisterRequest;
import com.defty.movie.dto.request.UserRequest;
import com.defty.movie.dto.response.*;
import com.defty.movie.service.IAuthUserService;
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
@RequestMapping("${api.prefix}/user/auth")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthUserController {
    IAuthUserService authUserService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, HttpServletResponse res){
        LoginResponse loginResponse = authUserService.login(loginRequest, res);
        ApiResponse<LoginResponse> response = ApiResponse.<LoginResponse>builder()
                .status(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .data(loginResponse)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest){
        UserResponse userResponse = authUserService.register(registerRequest);
        ApiResponse<?> response = ApiResponse.builder()
                .status(HttpStatus.CREATED.value())
                .message(HttpStatus.CREATED.getReasonPhrase())
                .data(userResponse)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/check-account")
    public ResponseEntity<?> checkAccount(HttpServletRequest request) {
        String token = CookieUtil.getValue(request, "access_token");
        if (token == null || token.isEmpty()) {
            ApiResponse<?> response = ApiResponse.builder()
                    .status(HttpStatus.UNAUTHORIZED.value())
                    .message("Not logged in or do not have a valid token")
                    .data("ok")
                    .data(null)
                    .build();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
        try {
            UserResponse userResponse = authUserService.getUserFromToken(token);
            ApiResponse<?> response = ApiResponse.builder()
                    .status(HttpStatus.OK.value())
                    .message(HttpStatus.OK.getReasonPhrase())
                    .data(userResponse)
                    .build();
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
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
        RefreshTokenResponse newToken = authUserService.refreshToken(refreshToken, res);
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
        authUserService.logout(accessToken);
        CookieUtil.clear(response, "access_token");
        CookieUtil.clear(response, "refresh_token");
        ApiResponse<?> responseObj = ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .data(null)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(responseObj);
    }
}
