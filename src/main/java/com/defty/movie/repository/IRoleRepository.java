package com.defty.movie.repository;

import com.defty.movie.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface IRoleRepository extends JpaRepository<Role,Integer> {
    Role findRoleByName(String roleName);
}
