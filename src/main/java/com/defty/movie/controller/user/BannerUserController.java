package com.defty.movie.controller.user;


import com.defty.movie.service.IBannerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/user/accessible/banner")
public class BannerUserController {
    private final IBannerService bannerService;
    String PREFIX_BANNER_CONTROLLER = "Banner controller | ";
    @GetMapping("/all")
    public  Object getBanners(Pageable pageable,
                              @RequestParam(name = "title", required = false) String title,
                              @RequestParam(name = "status", required = false) Integer status) {
        log.info(PREFIX_BANNER_CONTROLLER + "Going to get banners");
        return bannerService.getAllBanners(pageable, title, status);
    }
}