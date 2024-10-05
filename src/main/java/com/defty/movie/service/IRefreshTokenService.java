package com.defty.movie.service;

public interface IRefreshTokenService {
    String createRefreshToken(Integer accountId);
    void deleteRefreshToken(Integer accountId);
}
