package com.defty.movie.repository;

import com.defty.movie.entity.MovieComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface IMovieCommentRepository extends JpaRepository<MovieComment, Integer> {
    Optional<List<MovieComment>> findByParentMovieComment_Id(Integer movieCommentId);

    List<MovieComment> findAllByEpisodeIdAndStatus(Integer id,Integer status);
    Optional<MovieComment> findByIdAndStatus(Integer id, int status);
    Optional<List<MovieComment>> findByParentMovieCommentIdAndStatus(Integer movieCommentId, int status);
    Page<MovieComment> findByEpisodeIdAndParentMovieCommentIsNullAndStatus(Integer episodeId, int status, Pageable pageable);
    List<MovieComment> findByEpisodeIdAndParentMovieCommentIsNullAndStatus(Integer episodeId, int status);
    Page<MovieComment> findByParentMovieCommentIdAndStatus(Integer parentMovieCommentId, int status, Pageable pageable);
    long countByParentMovieCommentIdAndStatus(Integer parentMovieCommentId, int status);

    String SIMPLIFIED_BREADTH_FIRST_CTE_MYSQL =
            "WITH RECURSIVE comment_tree AS ( " +
                    "    SELECT mc.id, mc.content, mc.createddate, mc.status, mc.episode_id, mc.user_id, mc.parent_movie_comment_id, " +
                    "           mc.createdby, mc.modifieddate, mc.modifiedby, " + // Các cột audit
                    "           1 AS depth " + // Vẫn tính depth, nhưng không dùng để sort chính
                    "    FROM movie_comment mc " +
                    "    WHERE mc.parent_movie_comment_id = :parentCommentId AND mc.status = :status " +
                    "UNION ALL " +
                    "    SELECT child.id, child.content, child.createddate, child.status, child.episode_id, child.user_id, child.parent_movie_comment_id, " +
                    "           child.createdby, child.modifieddate, child.modifiedby, " + // Các cột audit
                    "           ct.depth + 1 AS depth " +
                    "    FROM movie_comment child " +
                    "    JOIN comment_tree ct ON child.parent_movie_comment_id = ct.id " +
                    "    WHERE child.status = :status " +
                    ") ";

    @Query(value = SIMPLIFIED_BREADTH_FIRST_CTE_MYSQL +
            // Chọn các cột cần thiết để map vào Entity MovieComment.
            // Cột 'depth' không được chọn ở đây vì không có trong entity và không dùng để sort.
            "SELECT id, content, createddate, status, episode_id, user_id, parent_movie_comment_id, " +
            "       createdby, modifieddate, modifiedby " + // Đảm bảo các cột này được chọn
            "FROM comment_tree " +
            // THAY ĐỔI CHÍNH: Sắp xếp theo thời gian tạo mới nhất lên đầu, sau đó theo ID (cũng mới nhất) để ổn định.
            "ORDER BY createddate DESC, id DESC",
            // countQuery không thay đổi, vẫn đếm tổng số hậu duệ.
            countQuery = SIMPLIFIED_BREADTH_FIRST_CTE_MYSQL + "SELECT count(*) FROM comment_tree",
            nativeQuery = true)
    Page<MovieComment> findCommentDescendantsBreadthFirstPaginated(
            @Param("parentCommentId") Integer parentCommentId,
            @Param("status") int status,
            Pageable pageable);

    // sap xep theo chieu xau, theo time, theo id
    // CTE cho MySQL 8.0+, bao gồm các cột audit từ BaseEntity
//    String SIMPLIFIED_BREADTH_FIRST_CTE_MYSQL =
//            "WITH RECURSIVE comment_tree AS ( " +
//                    "    SELECT mc.id, mc.content, mc.createddate, mc.status, mc.episode_id, mc.user_id, mc.parent_movie_comment_id, " +
//                    "           mc.createdby, mc.modifieddate, mc.modifiedby, " + // THÊM CÁC CỘT AUDIT
//                    "           1 AS depth " +
//                    "    FROM movie_comment mc " + // mc là bí danh cho bảng movie_comment
//                    "    WHERE mc.parent_movie_comment_id = :parentCommentId AND mc.status = :status " +
//                    "UNION ALL " +
//                    "    SELECT child.id, child.content, child.createddate, child.status, child.episode_id, child.user_id, child.parent_movie_comment_id, " +
//                    "           child.createdby, child.modifieddate, child.modifiedby, " + // THÊM CÁC CỘT AUDIT
//                    "           ct.depth + 1 AS depth " +
//                    "    FROM movie_comment child " + // child là bí danh cho bảng movie_comment
//                    "    JOIN comment_tree ct ON child.parent_movie_comment_id = ct.id " +
//                    "    WHERE child.status = :status " +
//                    ") ";
//
//    @Query(value = SIMPLIFIED_BREADTH_FIRST_CTE_MYSQL +
//            // Chọn tất cả các cột cần thiết để map vào Entity MovieComment
//            "SELECT id, content, createddate, status, episode_id, user_id, parent_movie_comment_id, " +
//            "       createdby, modifieddate, modifiedby, depth " + // Đảm bảo các cột này được chọn
//            "FROM comment_tree " +
//            "ORDER BY depth ASC, createddate ASC, id ASC",
//            countQuery = SIMPLIFIED_BREADTH_FIRST_CTE_MYSQL + "SELECT count(*) FROM comment_tree",
//            nativeQuery = true)
//    Page<MovieComment> findCommentDescendantsBreadthFirstPaginated(
//            @Param("parentCommentId") Integer parentCommentId,
//            @Param("status") int status,
//            Pageable pageable);
}