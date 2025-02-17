package com.defty.movie.controller.admin;

import com.defty.movie.dto.request.ShowonRequest;
import com.defty.movie.dto.response.ApiResponse;
import com.defty.movie.service.IShowonService;
import com.defty.movie.service.impl.ShowonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/admin/show-on")
public class ShowonController {
    private final IShowonService showonService;
    @PostMapping("")
    @PreAuthorize("@requiredPermission.checkPermission('CREATE_SHOWON')")
    public ApiResponse<Integer> addShowon(@ModelAttribute ShowonRequest showonRequest){
        return showonService.addShowon(showonRequest);

    }

    @PatchMapping("/{id}")
    @PreAuthorize("@requiredPermission.checkPermission('UPDATE_SHOWON')")
    public ApiResponse<Integer> patchShowon(@PathVariable Integer id, @ModelAttribute ShowonRequest showonRequest) {
        return showonService.updateShowon(id, showonRequest);
    }

    @GetMapping("/all")
    @PreAuthorize("@requiredPermission.checkPermission('GET_SHOWONS')")
    public  Object getShowons(Pageable pageable,
                             @RequestParam(name = "contentType", required = false) String contentType,
                             @RequestParam(name = "contentName", required = false) String contentName,
                             @RequestParam(name = "status", required = false) Integer status) {
        return showonService.getAllShowons(pageable, contentType,contentName, status);
    }

    @GetMapping("/{id}")
    @PreAuthorize("@requiredPermission.checkPermission('GET_SHOWON')")
    public Object getShowon(@PathVariable Integer id){
        return showonService.getShowon(id);
    }

    @DeleteMapping("/{ids}")
    @PreAuthorize("@requiredPermission.checkPermission('DELETE_SHOWON')")
    public ApiResponse<List<Integer>> deleteShowon(@PathVariable List<Integer> ids) {
        return showonService.deleteShowon(ids);
    }

    @PatchMapping("/status/{id}")
    @PreAuthorize("@requiredPermission.checkPermission('CHANGE_SHOWON_STATUS')")
    public ApiResponse<Integer> changeStatus(@PathVariable Integer id) {
        return showonService.changeStatus(id);
    }
}
