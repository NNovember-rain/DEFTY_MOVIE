package com.defty.movie.repository;

import com.defty.movie.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IAccountRepository extends JpaRepository<Account, Integer> {
    Optional<Account> findByUsername(String username);
}
