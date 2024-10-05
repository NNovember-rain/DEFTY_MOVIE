package com.defty.movie.service;

import com.defty.movie.dto.request.LoginRequest;
import com.defty.movie.dto.response.AccountResponse;
import com.defty.movie.dto.response.LoginResponse;
import com.defty.movie.dto.response.RefreshTokenResponse;
import com.defty.movie.entity.Account;

import java.util.Optional;

public interface IAccountService {
    LoginResponse login(LoginRequest loginRequest);
    void logout(String token);
    AccountResponse getAccountFromToken(String token);
    Optional<Account> getCurrentAccount();
    RefreshTokenResponse refreshToken(String refreshToken);
}
