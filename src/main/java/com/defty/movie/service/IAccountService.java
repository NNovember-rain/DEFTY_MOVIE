package com.defty.movie.service;

import com.defty.movie.dto.request.AccountRequest;
import com.defty.movie.dto.response.AccountResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IAccountService {
    AccountResponse createAccount(AccountRequest accountRequest);
    Page<AccountResponse> findAccount(String username, Pageable pageable);
    void deleteAccount(List<Integer> ids);
    AccountResponse updateAccount(Integer id, AccountRequest accountRequest);
    AccountResponse getAccount(Integer id);
}
