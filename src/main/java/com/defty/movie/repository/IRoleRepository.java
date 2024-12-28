package com.defty.movie.repository;

import com.defty.movie.entity.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IRoleRepository extends JpaRepository<Role,Integer> {
    @Query("SELECT r FROM Role r WHERE r.status >= 0")
    Page<Role> findAllWithStatus(Pageable pageable);
    Role findByName(String name);

    @Query("SELECT r FROM Role r " +
            "WHERE LOWER(r.name) " +
            "LIKE LOWER(CONCAT('%', :name, '%')) " +
            "AND r.status >= 0")
    Page<Role> findRole(@Param("name") String name, Pageable pageable);
}
