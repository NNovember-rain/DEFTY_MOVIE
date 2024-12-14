package com.defty.movie.service.impl;

import com.defty.movie.dto.request.AccountRequest;
import com.defty.movie.dto.response.AccountResponse;
import com.defty.movie.entity.Account;
import com.defty.movie.entity.Role;
import com.defty.movie.exception.AlreadyExitException;
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
import java.util.Optional;

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
            throw new AlreadyExitException("Account already exists");
        }
        if(accountRepository.findByEmail(accountRequest.getEmail()).isPresent()) {
            throw new AlreadyExitException("Account already exists");
        }
        if (accountRepository.findByPhone(accountRequest.getPhone()).isPresent()) {
            throw new AlreadyExitException("Account already exists");
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
        accountRepository.findByUsername(accountRequest.getUsername()).ifPresent(existingAccount -> {
            if (!existingAccount.getId().equals(id)) {
                throw new AlreadyExitException("Username already exists");
            }
        });
        accountRepository.findByEmail(accountRequest.getEmail()).ifPresent(existingAccount -> {
            if (!existingAccount.getId().equals(id)) {
                throw new AlreadyExitException("Email already exists");
            }
        });
        accountRepository.findByPhone(accountRequest.getPhone()).ifPresent(existingAccount -> {
            if (!existingAccount.getId().equals(id)) {
                throw new AlreadyExitException("Phone already exists");
            }
        });
        account.setUsername(accountRequest.getUsername());
        account.setFullName(accountRequest.getFullName());
        account.setEmail(accountRequest.getEmail());
        account.setPhone(accountRequest.getPhone());
        account.setAddress(accountRequest.getAddress());
        account.setGender(accountRequest.getGender());
        if (accountRequest.getPassword() != null && !accountRequest.getPassword().isEmpty()) {
            String encodedPassword = passwordEncoder.encode(accountRequest.getPassword());
            account.setPassword(encodedPassword);
        }
        Role role = roleRepository.findByName(accountRequest.getRole());
        account.setRole(role);
        String password = passwordEncoder.encode(accountRequest.getPassword());
        account.setPassword(password);
        if(accountRequest.getAvatar() != null) {
            try {
                account.setAvatar(uploadImageUtil.upload(accountRequest.getAvatar()));
            }catch (Exception e){
                throw new ImageUploadException("Could not upload the image, please try again later !");
            }
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
