package com.defty.movie.service;

import com.defty.movie.dto.request.ActorRequest;
import com.defty.movie.dto.response.ActorResponse;
import com.defty.movie.dto.response.ApiResponse;
import com.defty.movie.dto.response.DirectorResponse;
import com.defty.movie.dto.response.PageableResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IActorService {
    ApiResponse<Integer> addActor(ActorRequest actorRequest);
    ApiResponse<Integer> updateActor(Integer id, ActorRequest actorRequest);
    ApiResponse<List<Integer>> deleteActor(List<Integer> ids);
    ApiResponse<PageableResponse<ActorResponse>> getAllActors(Pageable pageable, String name, String gender, String date_of_birth, String nationality, Integer status);
    Object getActor(Integer id);
}
