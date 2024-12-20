package com.defty.movie.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ArticleRequest {

    Integer id;

    @NotNull(message = "required")
    String title;

    String content;

    int status;   // enum 0=đã xóa, 1 hoạt động, 2, chưa duyệt ...

    String author;

    MultipartFile thumbnail;

}

