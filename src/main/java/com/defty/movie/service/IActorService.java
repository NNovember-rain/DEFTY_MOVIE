package com.defty.movie.service;

import com.defty.movie.dto.request.ActorRequest;
import com.defty.movie.dto.response.ActorResponse;
import com.defty.movie.dto.response.PageableResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IActorService {
    ResponseEntity<String> addActor(ActorRequest actorRequest);
    ResponseEntity<String> updateActor(Integer id, ActorRequest actorRequest);
    ResponseEntity<String> deleteActor(List<Integer> ids);
    PageableResponse<ActorResponse> getAllActors(Pageable pageable);
    Object getActor(Integer id);
}
