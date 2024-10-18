package com.defty.movie.mapper;

import com.defty.movie.dto.request.AccountRequest;
import com.defty.movie.dto.response.AccountResponse;
import com.defty.movie.entity.Account;
import com.defty.movie.entity.Role;
import com.defty.movie.repository.IRoleRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AccountMapper {
    ModelMapper modelMapper;
    IRoleRepository roleRepository;
    PasswordEncoder passwordEncoder;

    public AccountResponse toAccountResponse(Account account) {
        AccountResponse accountResponse = modelMapper.map(account, AccountResponse.class);
        accountResponse.setRole(account.getRole().getName());
        return accountResponse;
    }

    public Account toAccount(AccountRequest accountRequest) {
        Account account = modelMapper.map(accountRequest, Account.class);
        Role role = roleRepository.findByName(accountRequest.getRole());
        account.setRole(role);
        account.setStatus(1);
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        return account;
    }
}
