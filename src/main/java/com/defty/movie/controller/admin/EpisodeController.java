package com.defty.movie.controller.admin;

import com.defty.movie.dto.request.EpisodeRequest;
import com.defty.movie.service.impl.EpisodeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
//@RequestMapping("${api.prefix}/movie")
@RequestMapping("/movie/episode")
public class EpisodeController {
    private final EpisodeService episodeService;
    @PostMapping("")
    public ResponseEntity<String> addEpisode(@RequestBody EpisodeRequest episodeRequest) {
        return null;
    }
//    @GetMapping("/{id}")
//    public Object getEpisode(@PathVariable Integer id){
//        return episodeService.getEpisode(id);
//    }
//    @GetMapping("")
//    public List<MovieResponseDTO> getEpisodes(){
//        return episodeService.getEpisodes();
//    }
//
//    @PatchMapping("/{id}")
//    public ResponseEntity<String> patchEpisode(@PathVariable Integer id, @RequestBody MovieRequest movieRequest) {
//        return episodeService.updateEpisode(id, movieRequest);
//    }
//
//    @DeleteMapping("/{ids}")
//    public ResponseEntity<String> deleteEpisode(@PathVariable List<Integer> ids) {
//        return episodeService.deleteEpisode(ids);
//    }
}
