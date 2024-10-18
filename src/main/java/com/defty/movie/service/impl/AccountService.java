package com.defty.movie.service.impl;

import com.defty.movie.dto.request.AccountRequest;
import com.defty.movie.dto.response.AccountResponse;
import com.defty.movie.entity.Account;
import com.defty.movie.exception.NotFoundException;
import com.defty.movie.mapper.AccountMapper;
import com.defty.movie.repository.IAccountRepository;
import com.defty.movie.service.IAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountService implements IAccountService {
    private final IAccountRepository accountRepository;
    private final AccountMapper accountMapper;

    @Override
    public AccountResponse createAccount(AccountRequest accountRequest) {
        if(accountRepository.findByUsername(accountRequest.getUsername()).isPresent()) {
            throw new RuntimeException("Account already exists");
        }
        if(accountRepository.findByEmail(accountRequest.getUsername()).isPresent()) {
            throw new RuntimeException("Account already exists");
        }
        if (accountRepository.findByPhone(accountRequest.getUsername()).isPresent()) {
            throw new RuntimeException("Account already exists");
        }
        Account account = accountMapper.toAccount(accountRequest);
        accountRepository.save(account);
        return accountMapper.toAccountResponse(account);
    }

    @Override
    public Page<AccountResponse> findAccount(String username, Pageable pageable) {
        if (username == null || username.isEmpty()) {
            return accountRepository.findAllWithStatus(pageable).map(accountMapper::toAccountResponse);
        } else {
            return accountRepository.findAccount(username, pageable)
                    .map(accountMapper::toAccountResponse);
        }
    }


    @Override
    public void deleteAccount(List<Integer> ids) {

    }

    @Override
    public AccountResponse updateAccount(Integer id, AccountRequest accountRequest) {
        return null;
    }

    @Override
    public AccountResponse getAccount(Integer id) {
        Account account = accountRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Account not found")
        );
        return accountMapper.toAccountResponse(account);
    }

}
