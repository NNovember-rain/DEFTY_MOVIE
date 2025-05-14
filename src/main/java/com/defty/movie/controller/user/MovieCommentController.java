package com.defty.movie.controller.user;

import com.defty.movie.dto.request.MovieCommentRequest;
import com.defty.movie.dto.request.MovieCommentUpdateRequest;
import com.defty.movie.dto.response.MovieCommentResponse;
import com.defty.movie.dto.response.PageableResponse;
import com.defty.movie.entity.MovieComment;
import com.defty.movie.service.IMovieCommentService;
import com.defty.movie.utils.ApiResponeUtil;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import com.defty.movie.view.Views;
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/user")
public class MovieCommentController {

    private final IMovieCommentService movieCommentService;
    String PREFIX_MOVIE_COMMENT = "MOVIE_COMMENT | ";

    @PostMapping("/accessible/movie-comment")
    public Object addMovieComment(@RequestBody MovieCommentRequest movieCommentRequest) {
        Integer commentId = movieCommentService.addMovieComment(movieCommentRequest);
        log.info(PREFIX_MOVIE_COMMENT + "Add Episode Comment successfully");
        return ApiResponeUtil.ResponseCreatedSuccess(commentId);
    }

    @PatchMapping("/movie-comment/{commentId}")
    public Object updateMovieComment(@PathVariable Integer commentId, @RequestBody MovieCommentUpdateRequest movieCommentUpdateRequest) {
        movieCommentService.updateMovieComment(commentId, movieCommentUpdateRequest);
        String massage="Updated episode comment";
        log.info(PREFIX_MOVIE_COMMENT + "Updated Episode Comment successfully");
        return ApiResponeUtil.ResponseOK(massage);
    }

    @DeleteMapping("/movie-comment/{commentId}")
    public Object deleteMovieComment(@PathVariable Integer commentId) {
        movieCommentService.deleteMovieComment(commentId);
        String massage="Deleted episode comment";
        log.info(PREFIX_MOVIE_COMMENT + "Deleted Episode Comment successfully");
        return ApiResponeUtil.ResponseOK(massage);
    }

    @GetMapping("/accessible/movie-comment/{episodeId}")
    public Object getMovieComment(@PathVariable Integer episodeId,
                                  @Valid @RequestParam(value = "page", defaultValue = "0") int page,
                                  @RequestParam(value = "size", defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        PageableResponse<MovieCommentResponse> movieComments= movieCommentService.getMovieComment(episodeId,pageable);
        log.info("{}Get all Episode Comment by movieId successfully", PREFIX_MOVIE_COMMENT);
        return ApiResponeUtil.ResponseOK(movieComments);
    }

    @GetMapping("/accessible/movie-comment/{commentId}/replies")
    public Object getMovieCommentReplies(
            @PathVariable Integer commentId,
            @Valid @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "5") int size) { // Có thể dùng size khác cho replies
        // Sắp xếp replies theo ngày tạo tăng dần thường hợp lý hơn để đọc theo thứ tự
        Pageable pageable = PageRequest.of(page, size);
        List<MovieCommentResponse> replies = movieCommentService.getMovieCommentReplies(commentId, pageable);
        log.info("{} Get replies for comment {} successfully", PREFIX_MOVIE_COMMENT, commentId);
        // Tương tự, xem xét trả về Page<> nếu cần thông tin phân trang đầy đủ
        return ApiResponeUtil.ResponseOK(replies);
    }
}
