package com.defty.movie.service.impl;

import com.defty.movie.entity.Account;
import com.defty.movie.entity.RefreshToken;
import com.defty.movie.entity.User;
import com.defty.movie.exception.NotFoundException;
import com.defty.movie.repository.IAccountRepository;
import com.defty.movie.repository.IRefreshTokenRepository;
import com.defty.movie.repository.IUserRepository;
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
    final IUserRepository userRepository;

    @Override
    public String createRefreshToken(Integer id, boolean isAccount) {
        String refreshToken = UUID.randomUUID().toString();
        Instant expiresAt = Instant.now().plusMillis(expirationRefreshToken * 1000L);
        Object entity = isAccount ?
                accountRepository.findById(id).orElseThrow(() -> new NotFoundException("Account not found")) :
                userRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found"));
        deleteExistingToken(entity, isAccount);
        saveRefreshToken(entity, refreshToken, expiresAt, isAccount);
        return refreshToken;
    }

    @Override
    @Transactional
    public void deleteRefreshToken(Integer id, boolean isAccount) {
        if (isAccount) {
            refreshTokenRepository.deleteByAccountId(id);
        } else {
            refreshTokenRepository.deleteByUserId(id);
        }
    }

    private void deleteExistingToken(Object entity, boolean isAccount) {
        if (isAccount) {
            Account account = (Account) entity;
            RefreshToken existingToken = refreshTokenRepository.findByAccountId(account.getId());
            if (existingToken != null) {
                refreshTokenRepository.delete(existingToken);
            }
        } else {
            User user = (User) entity;
            RefreshToken existingToken = refreshTokenRepository.findByUserId(user.getId());
            if (existingToken != null) {
                refreshTokenRepository.delete(existingToken);
            }
        }
    }

    private void saveRefreshToken(Object entity, String refreshToken, Instant expiresAt, boolean isAccount) {
        RefreshToken token = new RefreshToken();
        token.setRefreshToken(refreshToken);
        token.setExpiresAt(expiresAt);
        if (isAccount) {
            Account account = (Account) entity;
            token.setAccount(account);
        } else {
            User user = (User) entity;
            token.setUser(user);
        }
        refreshTokenRepository.save(token);
    }
}

