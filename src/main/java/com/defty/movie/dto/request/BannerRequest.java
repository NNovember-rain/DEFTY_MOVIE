package com.defty.movie.dto.request;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BannerRequest {
    String key;
    MultipartFile thumbnail;
    String title;
    String contentType;
    Integer contentId;
    Integer position;
    Integer status;
}
