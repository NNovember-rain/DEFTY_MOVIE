package com.defty.movie.service;

import com.defty.movie.dto.Request.LoginRequest;


public interface IAccountService {
    String login(LoginRequest loginRequest) throws Exception;
}
