package com.defty.movie.service;

import com.defty.movie.dto.request.LoginRequest;


public interface IAccountService {
    String login(LoginRequest loginRequest) throws Exception;
}
