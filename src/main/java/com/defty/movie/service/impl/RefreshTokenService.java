package com.defty.movie.service.impl;

import com.defty.movie.entity.Account;
import com.defty.movie.entity.RefreshToken;
import com.defty.movie.exception.NotFoundException;
import com.defty.movie.repository.IAccountRepository;
import com.defty.movie.repository.IRefreshTokenRepository;
import com.defty.movie.service.IRefreshTokenService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RefreshTokenService implements IRefreshTokenService {
    @Value("${jwt.expiration-refresh-token}")
    private int expirationRefreshToken;

    final IRefreshTokenRepository refreshTokenRepository;
    final IAccountRepository accountRepository;

    //TODO: Tao 1 refreshToken cho nguoi dung
    @Override
    public String createRefreshToken(Integer accountId) {
        String refreshToken = UUID.randomUUID().toString();
        Instant expiresAt = Instant.now().plusMillis(expirationRefreshToken * 1000L);
        Account account = accountRepository.findById(accountId).orElseThrow(
                () -> new NotFoundException("Account not found")
        );
        RefreshToken existingToken = refreshTokenRepository.findByAccountId(accountId);
        if (existingToken != null) {
            refreshTokenRepository.delete(existingToken);
        }
        RefreshToken token = new RefreshToken();
        token.setAccount(account);
        token.setRefreshToken(refreshToken);
        token.setExpiresAt(expiresAt);
        refreshTokenRepository.save(token);
        return refreshToken;
    }

    @Override
    @Transactional
    public void deleteRefreshToken(Integer accountId) {
        refreshTokenRepository.deleteByAccountId(accountId);
    }
}
