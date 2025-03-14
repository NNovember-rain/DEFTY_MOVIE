package com.defty.movie.dto.response;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.Map;
import java.util.Set;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BannerResponse {
    Integer id;
    String key;
    String thumbnail;
    String title;
    String link;
    String contentType;
    Integer contentId;
    Integer position;
    Integer status;
    String contentName;
    String contentSlug;
    SubBannerResponse subBannerResponse;
    Map<String, String> bannerItems;
}
