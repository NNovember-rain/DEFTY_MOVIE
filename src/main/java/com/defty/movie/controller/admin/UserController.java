package com.defty.movie.controller.admin;

import com.defty.movie.dto.response.ApiResponse;
import com.defty.movie.service.IActorService;
import com.defty.movie.service.IUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/admin/user")
public class UserController {
    private final IUserService userService;
    @GetMapping("")
    @PreAuthorize("@requiredPermission.checkPermission('GET_USERS')")
    public  Object getActors(Pageable pageable,
                             @RequestParam(name = "name", required = false) String name,
                             @RequestParam(name = "gender", required = false) String gender,
                             @RequestParam(name = "date_of_birth", required = false) String date_of_birth,
                             @RequestParam(name = "nationality", required = false) String nationality,
                             @RequestParam(name = "status", required = false) Integer status) {
        return userService.getAllUsers(pageable, name, gender, date_of_birth, nationality, status);
    }

    @PatchMapping("/status/{id}")
    @PreAuthorize("@requiredPermission.checkPermission('CHANGE_USER_STATUS')")
    public ApiResponse<Integer> changeStatus(@PathVariable Integer id) {
        return userService.changeStatus(id);
    }

    @GetMapping("/{id}")
    @PreAuthorize("@requiredPermission.checkPermission('GET_USER')")
    public Object getUser(@PathVariable Integer id){
        return userService.getUser(id);
    }
}
