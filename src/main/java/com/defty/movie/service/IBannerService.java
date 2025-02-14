package com.defty.movie.service;

import com.defty.movie.dto.request.BannerRequest;
import com.defty.movie.dto.response.BannerResponse;
import com.defty.movie.dto.response.ApiResponse;
import com.defty.movie.dto.response.PageableResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

public interface IBannerService {
    ApiResponse<Integer> addBanner(BannerRequest bannerRequest);
    ApiResponse<Integer> updateBanner(Integer id, BannerRequest bannerRequest);
    ApiResponse<List<Integer>> deleteBanner(List<Integer> ids);
    ApiResponse<Integer> changeStatus(Integer id);
    ApiResponse<PageableResponse<BannerResponse>> getAllBanners(Pageable pageable, String title, Integer status);
    Object getBanner(Integer id);
}
