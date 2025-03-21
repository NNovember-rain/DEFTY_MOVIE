package com.defty.movie.controller.user;


import com.defty.movie.dto.request.ShowonRequest;
import com.defty.movie.dto.response.ApiResponse;
import com.defty.movie.service.IShowonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/user/accessible/show-on")
public class ShowonUserController {
    private final IShowonService showonService;

    @GetMapping("/all")
    public  Object getShowons(Pageable pageable,
                              @RequestParam(name = "contentType", required = false) String contentType,
                              @RequestParam(name = "contentName", required = false) String contentName,
                              @RequestParam(name = "status", required = false) Integer status) {
        return showonService.getAllShowons(pageable, contentType,contentName, status);
    }
}

