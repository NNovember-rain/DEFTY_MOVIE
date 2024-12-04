package com.defty.movie.service.impl;

import com.defty.movie.dto.request.LoginRequest;
import com.defty.movie.dto.request.RegisterRequest;
import com.defty.movie.dto.request.UserRequest;
import com.defty.movie.dto.response.AccountResponse;
import com.defty.movie.dto.response.LoginResponse;
import com.defty.movie.dto.response.RefreshTokenResponse;
import com.defty.movie.dto.response.UserResponse;
import com.defty.movie.entity.Account;
import com.defty.movie.entity.RefreshToken;
import com.defty.movie.entity.User;
import com.defty.movie.exception.AlreadyExitException;
import com.defty.movie.exception.NotFoundException;
import com.defty.movie.mapper.AccountMapper;
import com.defty.movie.mapper.UserMapper;
import com.defty.movie.repository.IAccountRepository;
import com.defty.movie.repository.IRefreshTokenRepository;
import com.defty.movie.repository.IUserRepository;
import com.defty.movie.security.JwtTokenUtil;
import com.defty.movie.service.IAuthUserService;
import com.defty.movie.service.IRefreshTokenService;
import com.defty.movie.utils.CookieUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthUserService implements IAuthUserService {
    @Value("${jwt.expiration}")
    private Integer expirationAccessToken;
    @Value("${jwt.expiration-refresh-token}")
    private Integer expirationRefreshToken;
    private final IAccountRepository accountRepository;
    private final UserMapper userMapper;
    private final IRefreshTokenService refreshTokenService;
    private final IRefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;
    private final IUserRepository userRepository;


    @Override
    public LoginResponse login(LoginRequest loginRequest, HttpServletResponse response) {
        Optional<User> userOptional = userRepository.findByUsername(loginRequest.getUsername());
        if (userOptional.isEmpty()) {
            throw new NotFoundException("User not found");
        }
        User user = userOptional.get();
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new RuntimeException("Wrong username or password");
        }
        String accessToken = jwtTokenUtil.generateToken(user);
        String refreshToken = refreshTokenService.createRefreshToken(user.getId(), false);
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
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new NotFoundException("User not found")
        );
        if(user != null) {
            refreshTokenService.deleteRefreshToken(user.getId(), false);
        }
    }

    @Override
    public UserResponse getUserFromToken(String token) {
        if(jwtTokenUtil.isTokenExpired(token)) {
            throw new RuntimeException("Token is expired");
        }
        String username = jwtTokenUtil.extractUsername(token);
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isEmpty()) {
            throw new NotFoundException("User not found");
        }
        User user = userOptional.get();
        return userMapper.toUserResponse(user);
    }

    @Override
    public UserResponse register(RegisterRequest registerRequest) {
        if (userRepository.findByUsername(registerRequest.getUsername()).isPresent()) {
            throw new AlreadyExitException("Username already exists");
        }
        if(accountRepository.findByUsername(registerRequest.getUsername()).isPresent()){
            throw new AlreadyExitException("Username already exits");
        }
        if(userRepository.findByEmail(registerRequest.getEmail()).isPresent()){
            throw new AlreadyExitException("Email already exit");
        }
        if(userRepository.findByPhone(registerRequest.getPhone()).isPresent()){
            throw new AlreadyExitException("Phone already exit");
        }
        String encodedPassword = passwordEncoder.encode(registerRequest.getPassword());
        User user = User.builder()
                .phone(registerRequest.getPhone())
                .username(registerRequest.getUsername())
                .fullName(registerRequest.getFullName())
                .gender(registerRequest.getGender())
                .dateOfBirth(registerRequest.getDateOfBirth())
                .address(registerRequest.getAddress())
                .email(registerRequest.getEmail())
                .password(encodedPassword)
                .build();
        user.setPassword(encodedPassword);
        userRepository.save(user);
        return userMapper.toUserResponse(user);
    }

    @Override
    public RefreshTokenResponse refreshToken(String refreshToken, HttpServletResponse response) {
        Optional<RefreshToken> exitRefreshToken = refreshTokenRepository.findByRefreshToken(refreshToken);
        if (exitRefreshToken.isEmpty()) {
            throw new NotFoundException("Refresh token not found");
        }
        User user = exitRefreshToken.get().getUser();
        String newToken = jwtTokenUtil.generateToken(user);
        String newRefreshToken = refreshTokenService.createRefreshToken(user.getId(), false);
        CookieUtil.create(response, "access_token", newToken, true, true, expirationAccessToken, "/");
        CookieUtil.create(response, "refresh_token", newRefreshToken, true, true, expirationRefreshToken, "/");
        return RefreshTokenResponse.builder()
                .refreshToken(newRefreshToken)
                .token(newToken)
                .build();
    }
}
