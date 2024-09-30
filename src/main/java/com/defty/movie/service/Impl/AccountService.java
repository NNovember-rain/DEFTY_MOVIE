package com.defty.movie.service.Impl;

import com.defty.movie.dto.Request.LoginRequest;
import com.defty.movie.entity.Account;
import com.defty.movie.repository.IAccountRepository;
import com.defty.movie.security.JwtTokenUtil;
import com.defty.movie.service.IAccountService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AccountService implements IAccountService {
    IAccountRepository accountRepository;
    AuthenticationManager authenticationManager;
    PasswordEncoder passwordEncoder;
    JwtTokenUtil jwtTokenUtil;

    @Override
    public String login(LoginRequest loginRequest) throws Exception {
        Account account = accountRepository.findByUsername(loginRequest.getUsername());
        if(!passwordEncoder.matches(loginRequest.getPassword(), account.getPassword())) {
            throw new RuntimeException("Wrong password");
        }
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                loginRequest.getUsername(), loginRequest.getPassword(), account.getAuthorities());
        authenticationManager.authenticate(authenticationToken);
        return jwtTokenUtil.generateToken(account);
    }
}
