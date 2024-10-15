package com.defty.movie.service.impl;

import com.defty.movie.Util.CookieUtil;
import com.defty.movie.dto.request.LoginRequest;
import com.defty.movie.dto.response.AccountResponse;
import com.defty.movie.dto.response.LoginResponse;
import com.defty.movie.dto.response.RefreshTokenResponse;
import com.defty.movie.entity.Account;
import com.defty.movie.entity.RefreshToken;
import com.defty.movie.mapper.AccountMapper;
import com.defty.movie.repository.IAccountRepository;
import com.defty.movie.repository.IRefreshTokenRepository;
import com.defty.movie.security.JwtTokenUtil;
import com.defty.movie.service.IAccountService;
import com.defty.movie.service.IRefreshTokenService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class AccountService implements IAccountService {
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

    @Override
    public LoginResponse login(LoginRequest loginRequest, HttpServletResponse response) {
        Optional<Account> accountOptional = accountRepository.findByUsername(loginRequest.getUsername());
        if (accountOptional.isEmpty()) {
            throw new RuntimeException("Account not found");
        }
        Account account = accountOptional.get();
        if (!passwordEncoder.matches(loginRequest.getPassword(), account.getPassword())) {
            throw new RuntimeException("Wrong username or password");
        }
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                loginRequest.getUsername(), loginRequest.getPassword(), account.getAuthorities());
        authenticationManager.authenticate(authenticationToken);
        String accessToken = jwtTokenUtil.generateToken(account);
        String refreshToken = refreshTokenService.createRefreshToken(account.getId());
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
        Account account = accountRepository.findByUsername(username).orElseThrow(
                () -> new RuntimeException("Account not found")
        );
        if(account != null) {
            refreshTokenService.deleteRefreshToken(account.getId());
        }
    }

    @Override
    public AccountResponse getAccountFromToken(String token) {
        if(jwtTokenUtil.isTokenExpired(token)) {
            throw new RuntimeException("Token is expired");
        }
        String username = jwtTokenUtil.extractUsername(token);
        Optional<Account> accountOptional = accountRepository.findByUsername(username);
        if (accountOptional.isEmpty()) {
            throw new RuntimeException("Account not found");
        }
        Account account = accountOptional.get();
        return accountMapper.toAccountResponse(account);
    }

    @Override
    public Optional<Account> getCurrentAccount() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || auth.getPrincipal().equals("anonymousUser")) {
            return Optional.empty();
        }
        return (Optional<Account>) auth.getPrincipal();
    }

    @Override
    public RefreshTokenResponse refreshToken(String refreshToken, HttpServletResponse response) {
        Optional<RefreshToken> exitRefreshToken = refreshTokenRepository.findByRefreshToken(refreshToken);
        if (exitRefreshToken.isEmpty()) {
            throw new RuntimeException("Refresh token not found");
        }
        Account account = exitRefreshToken.get().getAccount();
        String newToken = jwtTokenUtil.generateToken(account);
        String newRefreshToken = refreshTokenService.createRefreshToken(account.getId());
        CookieUtil.create(response, "access_token", newToken, true, true, expirationAccessToken, "/");
        CookieUtil.create(response, "refresh_token", newRefreshToken, true, true, expirationRefreshToken, "/");
        return RefreshTokenResponse.builder()
                .refreshToken(newRefreshToken)
                .token(newToken)
                .build();
    }
}
