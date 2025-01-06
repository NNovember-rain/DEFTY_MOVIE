package com.defty.movie.service;

import com.defty.movie.dto.response.UserResponse;
import com.defty.movie.dto.response.ApiResponse;
import com.defty.movie.dto.response.PageableResponse;
import org.springframework.data.domain.Pageable;

public interface IUserService {
    ApiResponse<PageableResponse<UserResponse>> getAllUsers(Pageable pageable, String name, String gender, String date_of_birth, String nationality, Integer status);
}
