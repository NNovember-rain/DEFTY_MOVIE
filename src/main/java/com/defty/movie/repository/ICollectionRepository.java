package com.defty.movie.repository;

import com.defty.movie.entity.Collection;
import com.defty.movie.entity.Collection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ICollectionRepository extends JpaRepository<Collection, Integer> {
    @Query(value = "SELECT m FROM Collection m WHERE " +
            "(:name IS NULL OR m.name LIKE %:name%) AND" +
            "(m.status != -1) AND " +
            "(:status IS NULL OR m.status = :status) " +
            "ORDER BY m.createdDate DESC",
            countQuery = "SELECT count(m) FROM Collection m WHERE " +
                    "(:name IS NULL OR m.name LIKE %:name%) AND " +
                    "(m.status != -1) AND " +
                    "(:status IS NULL OR m.status = :status)",
            nativeQuery = false)
    Page<Collection> findCollections(
            @Param("name") String name,
            @Param("status") Integer status,
            Pageable pageable);
}