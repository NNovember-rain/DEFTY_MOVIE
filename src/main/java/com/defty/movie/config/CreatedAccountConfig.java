package com.defty.movie.config;

import com.defty.movie.entity.Account;
import com.defty.movie.entity.Role;
import com.defty.movie.repository.IAccountRepository;
import com.defty.movie.repository.IRoleRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j

public class CreatedAccountConfig {

    PasswordEncoder passwordEncoder;
    IRoleRepository roleRepository;

    @Bean
    ApplicationRunner createAccount(IAccountRepository accountRepository) {
        return args -> {
            if(accountRepository.findByUsername("vannv") == null) {
                Role role =  roleRepository.findRoleByName("ADMIN");
                Account account = Account.builder()
                        .username("vannv")
                        .password(passwordEncoder.encode("123456"))
                        .role(role)
                        .build();
                accountRepository.save(account);
                log.info("Created account successfully: {}", account.getUsername());
            }
        };
    }
}
