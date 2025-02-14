package com.defty.movie.repository;

import com.defty.movie.entity.MembershipPackage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IMembershipPackageRepository extends JpaRepository<MembershipPackage, Integer> {
    @Query("SELECT r FROM MembershipPackage r WHERE r.status >= 0")
    Page<MembershipPackage> findAllWithStatus(Pageable pageable);
    MembershipPackage findByName(String name);

    @Query("SELECT r FROM MembershipPackage r " +
            "WHERE LOWER(r.name) " +
            "LIKE LOWER(CONCAT('%', :name, '%')) " +
            "AND r.status >= 0")
    Page<MembershipPackage> findMembershipPackage(@Param("name") String name, Pageable pageable);
}
