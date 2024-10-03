package com.defty.movie.service;

import com.defty.movie.dto.request.LoginRequest;
import com.defty.movie.dto.response.AccountResponse;
import com.defty.movie.entity.Account;

import java.util.Optional;

public interface IAccountService {
    String login(LoginRequest loginRequest);
    AccountResponse getAccountFromToken(String token);
    Optional<Account> getCurrentAccount();
}
