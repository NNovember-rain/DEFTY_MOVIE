package com.defty.movie.controller.admin;

import com.defty.movie.dto.request.DirectorRequest;
import com.defty.movie.service.impl.DirectorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
//@RequestMapping("${api.prefix}/movie")
@RequestMapping("/movie/director")
public class DirectorController {
    private final DirectorService directorService;
    @PostMapping("")
    public ResponseEntity<String> addDirector(@ModelAttribute DirectorRequest directorRequest){
        return directorService.addDirector(directorRequest);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<String> patchDirector(@PathVariable Integer id, @ModelAttribute DirectorRequest directorRequest) {
        return directorService.updateDirector(id, directorRequest);
    }

    @GetMapping("/all")
    public  Object getDirectors(Pageable pageable) {
        return directorService.getAllDirectors(pageable);
    }

    @GetMapping("/{id}")
    public Object getDirector(@PathVariable Integer id){
        return directorService.getDirector(id);
    }
}
