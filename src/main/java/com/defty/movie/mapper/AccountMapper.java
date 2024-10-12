package com.defty.movie.mapper;

import com.defty.movie.dto.response.AccountResponse;
import com.defty.movie.entity.Account;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AccountMapper {
    ModelMapper modelMapper;
    public AccountResponse toAccountResponse(Account account) {
        AccountResponse accountResponse = modelMapper.map(account, AccountResponse.class);
        accountResponse.setRole(account.getRole().getName());
        return accountResponse;
    }
}
