package com.defty.movie.controller.admin;

import com.defty.movie.dto.request.BannerRequest;
import com.defty.movie.dto.response.ApiResponse;
import com.defty.movie.service.IBannerService;
import com.defty.movie.service.IBannerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/banner")
public class BannerController {
    private final IBannerService bannerService;
    @PostMapping("")
    @PreAuthorize("@requiredPermission.checkPermission('CREATE_BANNER')")
    public ApiResponse<Integer> addBanner(@ModelAttribute BannerRequest bannerRequest){
        return bannerService.addBanner(bannerRequest);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("@requiredPermission.checkPermission('UPDATE_BANNER')")
    public ApiResponse<Integer> patchBanner(@PathVariable Integer id, @ModelAttribute BannerRequest bannerRequest) {
        return bannerService.updateBanner(id, bannerRequest);
    }

    @GetMapping("/all")
    @PreAuthorize("@requiredPermission.checkPermission('GET_BANNERS')")
    public  Object getBanners(Pageable pageable,
                             @RequestParam(name = "title", required = false) String title,
                             @RequestParam(name = "status", required = false) Integer status) {
        return bannerService.getAllBanners(pageable, title, status);
    }

    @GetMapping("/{id}")
    @PreAuthorize("@requiredPermission.checkPermission('GET_BANNER')")
    public Object getBanner(@PathVariable Integer id){
        return bannerService.getBanner(id);
    }

    @DeleteMapping("/{ids}")
    @PreAuthorize("@requiredPermission.checkPermission('DELETE_BANNER')")
    public ApiResponse<List<Integer>> deleteBanner(@PathVariable List<Integer> ids) {
        return bannerService.deleteBanner(ids);
    }

    @PatchMapping("/status/{id}")
    @PreAuthorize("@requiredPermission.checkPermission('CHANGE_BANNER_STATUS')")
    public ApiResponse<Integer> changeStatus(@PathVariable Integer id) {
        return bannerService.changeStatus(id);
    }
}
