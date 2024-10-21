package com.defty.movie.service;

import com.defty.movie.dto.request.DirectorRequest;
import com.defty.movie.dto.response.DirectorResponse;
import com.defty.movie.dto.response.PageableResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IDirectorService {
    ResponseEntity<String> addDirector(DirectorRequest directorRequest);
    ResponseEntity<String> updateDirector(Integer id, DirectorRequest directorRequest);
    ResponseEntity<String> deleteDirector(List<Integer> ids);
    PageableResponse<DirectorResponse> getAllDirectors(Pageable pageable);
    Object getDirector(Integer id);
}
