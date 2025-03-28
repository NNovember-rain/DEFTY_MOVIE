package com.defty.movie.service;

import com.defty.movie.dto.request.ShowonRequest;
//import com.defty.movie.dto.response.ShowonResponse;
import com.defty.movie.dto.response.ApiResponse;
import com.defty.movie.dto.response.PageableResponse;
import com.defty.movie.dto.response.ShowonResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IShowonService {
    ApiResponse<Integer> addShowon(ShowonRequest actorRequest);
    ApiResponse<Integer> updateShowon(Integer id, ShowonRequest showonRequest);
    ApiResponse<List<Integer>> deleteShowon(List<Integer> ids);
    ApiResponse<Integer> changeStatus(Integer id);
    ApiResponse<PageableResponse<ShowonResponse>> getAllShowons(Pageable pageable,String contentType, String contentName, Integer status);
    Object getShowon(Integer id);
}
