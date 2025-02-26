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
@RequestMapping("${api.prefix}/admin/banner")
public class BannerController {
    private final IBannerService bannerService;
    String PREFIX_BANNER_CONTROLLER = "Banner controller | ";
    @PostMapping("")
    @PreAuthorize("@requiredPermission.checkPermission('CREATE_BANNER')")
    public ApiResponse<Integer> addBanner(@ModelAttribute BannerRequest bannerRequest){
        log.info(PREFIX_BANNER_CONTROLLER + "Going to create banner");
        return bannerService.addBanner(bannerRequest);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("@requiredPermission.checkPermission('UPDATE_BANNER')")
    public ApiResponse<Integer> patchBanner(@PathVariable Integer id, @ModelAttribute BannerRequest bannerRequest) {
        log.info(PREFIX_BANNER_CONTROLLER + "Going to update banner");
        return bannerService.updateBanner(id, bannerRequest);
    }

    @GetMapping("/all")
    @PreAuthorize("@requiredPermission.checkPermission('GET_BANNERS')")
    public  Object getBanners(Pageable pageable,
                             @RequestParam(name = "title", required = false) String title,
                             @RequestParam(name = "status", required = false) Integer status) {
        log.info(PREFIX_BANNER_CONTROLLER + "Going to get banners");
        return bannerService.getAllBanners(pageable, title, status);
    }

    @GetMapping("/{id}")
    @PreAuthorize("@requiredPermission.checkPermission('GET_BANNER')")
    public Object getBanner(@PathVariable Integer id){
        log.info(PREFIX_BANNER_CONTROLLER + "Going to get banner");
        return bannerService.getBanner(id);
    }

    @DeleteMapping("/{ids}")
    @PreAuthorize("@requiredPermission.checkPermission('DELETE_BANNER')")
    public ApiResponse<List<Integer>> deleteBanner(@PathVariable List<Integer> ids) {
        log.info(PREFIX_BANNER_CONTROLLER + "Going to delete banner");
        return bannerService.deleteBanner(ids);
    }

    @PatchMapping("/status/{id}")
    @PreAuthorize("@requiredPermission.checkPermission('CHANGE_BANNER_STATUS')")
    public ApiResponse<Integer> changeStatus(@PathVariable Integer id) {
        log.info(PREFIX_BANNER_CONTROLLER + "Going to change banner status");
        return bannerService.changeStatus(id);
    }
}
