package com.defty.movie.repository;

import com.defty.movie.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IRefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByRefreshToken(String refreshToken);
    RefreshToken findByAccountId(Integer accountId);
    RefreshToken findByUserId(Integer userId);
    void deleteByAccountId(Integer accountId);
    void deleteByUserId(Integer userId);
}
