package com.defty.movie.service;


import com.defty.movie.dto.request.LoginRequest;
import com.defty.movie.dto.request.RegisterRequest;
import com.defty.movie.dto.request.UserRequest;
import com.defty.movie.dto.response.AccountResponse;
import com.defty.movie.dto.response.LoginResponse;
import com.defty.movie.dto.response.RefreshTokenResponse;
import com.defty.movie.dto.response.UserResponse;
import com.defty.movie.entity.Account;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Optional;

public interface IAuthUserService {
    LoginResponse login(LoginRequest loginRequest, HttpServletResponse response);
    void logout(String token);
    UserResponse getUserFromToken(String token);
    UserResponse register(RegisterRequest registerRequest);
    RefreshTokenResponse refreshToken(String refreshToken, HttpServletResponse response);
}
