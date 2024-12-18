package com.defty.movie.service;

import com.defty.movie.dto.request.DirectorRequest;
import com.defty.movie.dto.response.ApiResponse;
import com.defty.movie.dto.response.DirectorResponse;
import com.defty.movie.dto.response.PageableResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IDirectorService {
    ApiResponse<Integer> addDirector(DirectorRequest directorRequest);
    ApiResponse<Integer> updateDirector(Integer id, DirectorRequest directorRequest);
    ApiResponse<List<Integer>> deleteDirector(List<Integer> ids);
    ApiResponse<PageableResponse<DirectorResponse>> getAllDirectors(Pageable pageable,String name, String gender, String date_of_birth, String nationality, Integer status);
    Object getDirector(Integer id);
}
