package com.defty.movie.repository;

import com.defty.movie.entity.User;
import com.defty.movie.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Optional;

@Repository
public interface IUserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    Optional<User> findByPhone(String phone);

    @Query(value = "SELECT m FROM User m WHERE " +
            "(:name IS NULL OR m.fullName LIKE %:name%) AND " +
            "(:gender IS NULL OR m.gender LIKE %:gender%) AND " +
            "((:startDate IS NULL AND :endDate IS NULL) OR " +
            "(DATE(m.dateOfBirth) BETWEEN DATE(:startDate) AND DATE(:endDate))) AND " +
            "(m.status != -1) AND " +
            "(:status IS NULL OR m.status = :status) " +
            "ORDER BY m.createdDate DESC",
            countQuery = "SELECT count(m) FROM User m WHERE " +
                    "(:name IS NULL OR m.fullName LIKE %:name%) AND " +
                    "(:gender IS NULL OR m.gender LIKE %:gender%) AND " +
                    "((:startDate IS NULL AND :endDate IS NULL) OR " +
                    "(DATE(m.dateOfBirth) BETWEEN DATE(:startDate) AND DATE(:endDate))) AND " +
                    "(m.status != -1) AND " +
                    "(:status IS NULL OR m.status = :status)",
            nativeQuery = false)
    Page<User> findUsers(
            @Param("name") String name,
            @Param("gender") String gender,
            @Param("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @Param("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
            @Param("status") Integer status,
            Pageable pageable);
}
