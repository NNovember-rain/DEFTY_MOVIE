package com.defty.movie.service.impl;

import com.defty.movie.dto.request.LoginRequest;
import com.defty.movie.dto.response.AccountResponse;
import com.defty.movie.dto.response.LoginResponse;
import com.defty.movie.dto.response.RefreshTokenResponse;
import com.defty.movie.entity.Account;
import com.defty.movie.entity.RefreshToken;
import com.defty.movie.exception.NotFoundException;
import com.defty.movie.mapper.AccountMapper;
import com.defty.movie.repository.IAccountRepository;
import com.defty.movie.repository.IRefreshTokenRepository;
import com.defty.movie.security.JwtTokenUtil;
import com.defty.movie.service.IAuthService;
import com.defty.movie.service.IRefreshTokenService;
import com.defty.movie.utils.CookieUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthService implements IAuthService {
    @Value("${jwt.expiration}")
    private Integer expirationAccessToken;
    @Value("${jwt.expiration-refresh-token}")
    private Integer expirationRefreshToken;
    private final IAccountRepository accountRepository;
    private final AuthenticationManager authenticationManager;
    private final IRefreshTokenService refreshTokenService;
    private final IRefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;
    private final AccountMapper accountMapper;
    String PREFIX_AUTH = "AUTH | ";

    @Override
    public LoginResponse login(LoginRequest loginRequest, HttpServletResponse response) {
        Optional<Account> accountOptional = accountRepository.findByUsername(loginRequest.getUsername());
        if (accountOptional.isEmpty()) {
            log.error("{}Account not found with username: {}", PREFIX_AUTH, loginRequest.getUsername());
            throw new NotFoundException("Account not found");
        }
        if (accountOptional.get().getStatus() == 0) {
            log.error("{}Account is disabled", PREFIX_AUTH);
            throw new RuntimeException("Account is disabled");
        }
        Account account = accountOptional.get();
        if (!passwordEncoder.matches(loginRequest.getPassword(), account.getPassword())) {
            log.error("{}Wrong username or password", PREFIX_AUTH);
            throw new RuntimeException("Wrong username or password");
        }
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                loginRequest.getUsername(), loginRequest.getPassword(), account.getAuthorities());
        authenticationManager.authenticate(authenticationToken);
        String accessToken = jwtTokenUtil.generateToken(account);
        String refreshToken = refreshTokenService.createRefreshToken(account.getId(), true);
        CookieUtil.create(response, "access_token", accessToken, true, true, expirationAccessToken, "/");
        CookieUtil.create(response, "refresh_token", refreshToken, true, true, expirationRefreshToken, "/");
        return LoginResponse.builder()
                .token(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public void logout(String token) {
        String username = jwtTokenUtil.extractUsername(token);
        Optional<Account> accountOptional = accountRepository.findByUsername(username);
        if(accountOptional.isEmpty()) {
            log.error("{}Account not found with username", PREFIX_AUTH);
            throw new NotFoundException("Account not found");
        }
        Account account = accountOptional.get();
        refreshTokenService.deleteRefreshToken(account.getId(), true);
    }

    @Override
    public AccountResponse getAccountFromToken(String token) {
        if(jwtTokenUtil.isTokenExpired(token)) {
            log.error("{}Token is expired", PREFIX_AUTH);
            throw new RuntimeException("Token is expired");
        }
        String username = jwtTokenUtil.extractUsername(token);
        Optional<Account> accountOptional = accountRepository.findByUsername(username);
        if (accountOptional.isEmpty()) {
            log.error("{}Account not found with username: {}", PREFIX_AUTH, username);
            throw new NotFoundException("Account not found");
        }
        Account account = accountOptional.get();
        return accountMapper.toAccountResponse(account);
    }

    @Override
    public Optional<Account> getCurrentAccount() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || auth.getPrincipal().equals("anonymousUser")) {
            log.error("{}Account not found", PREFIX_AUTH);
            return Optional.empty();
        }
        String username = ((UserDetails) auth.getPrincipal()).getUsername();
        return accountRepository.findByUsername(username);
    }

    @Override
    public RefreshTokenResponse refreshToken(String refreshToken, HttpServletResponse response) {
        Optional<RefreshToken> exitRefreshToken = refreshTokenRepository.findByRefreshToken(refreshToken);
        if (exitRefreshToken.isEmpty()) {
            log.error("{}Refresh token not found", PREFIX_AUTH);
            throw new NotFoundException("Refresh token not found");
        }
        Account account = exitRefreshToken.get().getAccount();
        String newToken = jwtTokenUtil.generateToken(account);
        String newRefreshToken = refreshTokenService.createRefreshToken(account.getId(), true);
        CookieUtil.create(response, "access_token", newToken, true, true, expirationAccessToken, "/");
        CookieUtil.create(response, "refresh_token", newRefreshToken, true, true, expirationRefreshToken, "/");
        return RefreshTokenResponse.builder()
                .refreshToken(newRefreshToken)
                .token(newToken)
                .build();
    }
}
