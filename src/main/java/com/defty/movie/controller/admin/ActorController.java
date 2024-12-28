package com.defty.movie.controller.admin;

import com.defty.movie.dto.request.ActorRequest;
import com.defty.movie.dto.response.ApiResponse;
import com.defty.movie.service.IActorService;
import com.defty.movie.service.impl.ActorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/movie/actor")
//@RequestMapping("/movie/actor")
public class ActorController {
    private final IActorService actorService;
    @PostMapping("")
    @PreAuthorize("@requiredPermission.checkPermission('CREATE_ACTOR')")
    public ApiResponse<Integer> addActor(@ModelAttribute ActorRequest actorRequest){
        return actorService.addActor(actorRequest);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("@requiredPermission.checkPermission('UPDATE_ACTOR')")
    public ApiResponse<Integer> patchActor(@PathVariable Integer id, @ModelAttribute ActorRequest actorRequest) {
        return actorService.updateActor(id, actorRequest);
    }

    @GetMapping("/all")
    @PreAuthorize("@requiredPermission.checkPermission('GET_ACTORS')")
    public  Object getActors(Pageable pageable,
                             @RequestParam(name = "name", required = false) String name,
                             @RequestParam(name = "gender", required = false) String gender,
                             @RequestParam(name = "date_of_birth", required = false) String date_of_birth,
                             @RequestParam(name = "nationality", required = false) String nationality,
                             @RequestParam(name = "status", required = false) Integer status) {
        return actorService.getAllActors(pageable, name, gender, date_of_birth, nationality, status);
    }

    @GetMapping("/{id}")
    @PreAuthorize("@requiredPermission.checkPermission('GET_ACTOR')")
    public Object getActor(@PathVariable Integer id){
        return actorService.getActor(id);
    }

    @DeleteMapping("/{ids}")
    @PreAuthorize("@requiredPermission.checkPermission('DELETE_ACTOR')")
    public ApiResponse<List<Integer>> deleteActor(@PathVariable List<Integer> ids) {
        return actorService.deleteActor(ids);
    }

    @PatchMapping("/do-enable/{ids}")
    @PreAuthorize("@requiredPermission.checkPermission('ENABLE_ACTOR')")
    public ApiResponse<List<Integer>> enableActor(@PathVariable List<Integer> ids) {
        return actorService.enableActor(ids);
    }

    @PatchMapping("/do-disable/{ids}")
    @PreAuthorize("@requiredPermission.checkPermission('DISABLE_ACTOR')")
    public ApiResponse<List<Integer>> disableActor(@PathVariable List<Integer> ids) {
        return actorService.disableActor(ids);
    }
}

