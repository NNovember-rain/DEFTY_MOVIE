package com.defty.movie.service;

import com.defty.movie.dto.request.LoginRequest;
import com.defty.movie.dto.response.AccountResponse;

public interface IAccountService {
    String login(LoginRequest loginRequest);
    AccountResponse getAccountFromToken(String token);
}
