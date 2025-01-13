package com.defty.movie.repository;

import com.defty.movie.entity.Banner;
import com.defty.movie.entity.Banner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface IBannerRepository extends JpaRepository<Banner, Integer> {
    @Query(value = "SELECT m FROM Banner m WHERE " +
            "(:title IS NULL OR m.title LIKE %:title%) AND" +
            "(m.status != -1) AND " +
            "(:status IS NULL OR m.status = :status) " +
            "ORDER BY m.createdDate DESC",
            countQuery = "SELECT count(m) FROM Banner m WHERE " +
                    "(:title IS NULL OR m.title LIKE %:title%) AND " +
                    "(m.status != -1) AND " +
                    "(:status IS NULL OR m.status = :status)",
            nativeQuery = false)
    Page<Banner> findBanners(
            @Param("title") String title,
            @Param("status") Integer status,
            Pageable pageable);
}
