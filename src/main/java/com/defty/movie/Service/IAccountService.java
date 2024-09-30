package com.defty.movie.Service;

import com.defty.movie.DTO.Request.LoginRequest;


public interface IAccountService {
    String login(LoginRequest loginRequest) throws Exception;
}
