package com.defty.movie.service;

public interface IRefreshTokenService {
    String createRefreshToken(Integer accountId, boolean isAccount);
    void deleteRefreshToken(Integer accountId, boolean isActive);
}
