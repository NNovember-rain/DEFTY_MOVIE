package com.defty.movie.repository;

import com.defty.movie.entity.Showon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface IShowonRepository extends JpaRepository<Showon, Integer> {
    @Query("SELECT s FROM Showon s " +
            "LEFT JOIN s.category c " +
            "WHERE (:contentType IS NULL OR s.contentType = :contentType) " +
            "AND (:contentName IS NULL OR (s.contentType = 'category' AND c.name LIKE %:contentName%)) " +
            "AND (:status IS NULL OR s.status = :status) " +
            "AND s.status <> -1")
    Page<Showon> getShowons(@Param("contentType") String contentType,
                               @Param("contentName") String contentName,
                               @Param("status") Integer status,
                               Pageable pageable);

//    @Query("SELECT s FROM Showon s " +
//            "LEFT JOIN s.category c " +
////            "LEFT JOIN s.playlist p " +
//            "WHERE (:contentType IS NULL OR s.contentType = :contentType) " +
//            "AND (:contentName IS NULL OR (s.contentType = 'category' AND c.name LIKE %:contentName%) " +
//            "OR (s.contentType = 'playlist' AND p.name LIKE %:contentName%)) " +
//            "AND (:status IS NULL OR s.status = :status)")
//    Page<Showon> searchShowons(@Param("contentType") String contentType,
//                               @Param("contentName") String contentName,
//                               @Param("status") Integer status,
//                               Pageable pageable);


}
