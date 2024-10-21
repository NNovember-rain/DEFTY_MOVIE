package com.defty.movie.controller.admin;

import com.defty.movie.dto.request.ActorRequest;
import com.defty.movie.service.impl.ActorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
//@RequestMapping("${api.prefix}/movie")
@RequestMapping("/movie/actor")
public class ActorController {
    private final ActorService actorService;
    @PostMapping("")
    public ResponseEntity<String> addActor(@ModelAttribute ActorRequest actorRequest){
        return actorService.addActor(actorRequest);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<String> patchActor(@PathVariable Integer id, @ModelAttribute ActorRequest actorRequest) {
        return actorService.updateActor(id, actorRequest);
    }

    @GetMapping("/all")
    public  Object getActors(Pageable pageable) {
        return actorService.getAllActors(pageable);
    }

    @GetMapping("/{id}")
    public Object getActor(@PathVariable Integer id){
        return actorService.getActor(id);
    }
}

