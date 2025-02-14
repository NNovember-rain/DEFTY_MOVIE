package com.defty.movie.mapper;

import com.defty.movie.dto.request.AccountRequest;
import com.defty.movie.dto.response.AccountResponse;
import com.defty.movie.entity.Account;
import com.defty.movie.entity.Role;
import com.defty.movie.exception.ImageUploadException;
import com.defty.movie.repository.IRoleRepository;
import com.defty.movie.utils.UploadImageUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AccountMapper {
    ModelMapper modelMapper;
    IRoleRepository roleRepository;
    PasswordEncoder passwordEncoder;
    UploadImageUtil uploadImageUtil;

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
        if(accountRequest.getAvatar() == null) {
            account.setAvatar(null);
        }
        else{
            try {
                account.setAvatar(uploadImageUtil.upload(accountRequest.getAvatar()));
            }catch (Exception e){
                String PREFIX_ACCOUNT = "ACCOUNT | ";
                log.error("{}Could not upload the image, please try again later !", PREFIX_ACCOUNT);
                throw new ImageUploadException("Could not upload the image, please try again later !");
            }
        }
        return account;
    }
}
