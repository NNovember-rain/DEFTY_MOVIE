package com.defty.movie.service.impl;

import com.defty.movie.dto.request.AccountRequest;
import com.defty.movie.dto.response.AccountResponse;
import com.defty.movie.entity.Account;
import com.defty.movie.entity.Role;
import com.defty.movie.exception.ImageUploadException;
import com.defty.movie.exception.NotFoundException;
import com.defty.movie.mapper.AccountMapper;
import com.defty.movie.repository.IAccountRepository;
import com.defty.movie.repository.IRoleRepository;
import com.defty.movie.service.IAccountService;
import com.defty.movie.utils.UploadImageUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AccountService implements IAccountService {
    IAccountRepository accountRepository;
    AccountMapper accountMapper;
    PasswordEncoder passwordEncoder;
    IRoleRepository roleRepository;
    UploadImageUtil uploadImageUtil;

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
        List<Account> accounts = accountRepository.findAllById(ids);
        for(Account account : accounts) {
            account.setStatus(0);
            accountRepository.save(account);
        }
    }

    @Override
    public AccountResponse updateAccount(Integer id, AccountRequest accountRequest) {
        Account account = accountRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Account not found")
        );
        account.setUsername(accountRequest.getUsername());
        account.setFullName(accountRequest.getFullName());
        account.setEmail(accountRequest.getEmail());
        account.setPhone(accountRequest.getPhone());
        account.setAddress(accountRequest.getAddress());
        account.setGender(accountRequest.getGender());
        Role role = roleRepository.findByName(accountRequest.getRole());
        account.setRole(role);
        String password = passwordEncoder.encode(accountRequest.getPassword());
        account.setPassword(password);
        try {
            account.setAvatar(uploadImageUtil.upload(accountRequest.getAvatar()));
        }catch (Exception e){
            throw new ImageUploadException("Could not upload the image, please try again later !");
        }
        account.setDateOfBirth(accountRequest.getDateOfBirth());
        accountRepository.save(account);
        return accountMapper.toAccountResponse(account);
    }

    @Override
    public AccountResponse getAccount(Integer id) {
        Account account = accountRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Account not found")
        );
        return accountMapper.toAccountResponse(account);
    }
}
