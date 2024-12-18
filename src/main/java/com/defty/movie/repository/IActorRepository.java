package com.defty.movie.repository;

import com.defty.movie.entity.Actor;
import com.defty.movie.entity.Director;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public interface IActorRepository extends JpaRepository<Actor, Integer> {
    @Query(value = "SELECT m FROM Actor m WHERE " +
            "(:name IS NULL OR m.fullName LIKE %:name%) AND " +
            "(:gender IS NULL OR m.gender LIKE %:gender%) AND " +
            "(:date_of_birth IS NULL OR DATE(m.dateOfBirth) = :date_of_birth) AND " +
            "(:nationality IS NULL OR m.nationality LIKE %:nationality%) AND " +
            "(:status IS NULL OR m.status = :status) " +
            "ORDER BY m.createdDate DESC",
            countQuery = "SELECT count(m) FROM Actor m WHERE " +
                    "(:name IS NULL OR m.fullName LIKE %:name%) AND " +
                    "(:gender IS NULL OR m.gender LIKE %:gender%) AND " +
                    "(:date_of_birth IS NULL OR DATE(m.dateOfBirth) = :date_of_birth) AND " +
                    "(:nationality IS NULL OR m.nationality LIKE %:nationality%) AND " +
                    "(:status IS NULL OR m.status = :status)",
            nativeQuery = false)
    Page<Actor> findActors(
            @Param("name") String name,
            @Param("gender") String gender,
            @Param("date_of_birth") @DateTimeFormat(pattern = "yyyy-MM-dd") Date date_of_birth,
            @Param("nationality") String nationality,
            @Param("status") Integer status,
            Pageable pageable);
}
