package com.defty.movie.service;

import jakarta.servlet.http.HttpServletResponse;

public interface IRefreshTokenService {
    String createRefreshToken(Integer accountId);
    void deleteRefreshToken(Integer accountId);
}
