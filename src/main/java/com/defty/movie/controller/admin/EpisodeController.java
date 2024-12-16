package com.defty.movie.controller.admin;

import com.defty.movie.dto.request.EpisodeRequest;
import com.defty.movie.dto.response.EpisodeResponse;
import com.defty.movie.service.impl.EpisodeService;
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
@RequestMapping("${api.prefix}/movie/episode")
public class EpisodeController {
    private final EpisodeService episodeService;
    @PostMapping("")
    @PreAuthorize("@requiredPermission.checkPermission('CREATE_EPISODE')")
    public ResponseEntity<String> addEpisode(@RequestBody EpisodeRequest episodeRequest) {
        return episodeService.addEpisode(episodeRequest);
    }
    @GetMapping("/{id}")
    @PreAuthorize("@requiredPermission.checkPermission('GET_EPISODE')")
    public Object getEpisode(@PathVariable Integer id){
        return episodeService.getEpisode(id);
    }
    @GetMapping("")
    @PreAuthorize("@requiredPermission.checkPermission('GET_EPISODES')")
    public Object getEpisodes(Pageable pageable,
                              @RequestParam(name = "number", required = false) Integer number,
                              @RequestParam(name = "status", required = false) Integer status,
                              @RequestParam(name = "movieId", required = false) Integer movieId){
        return episodeService.getEpisodes(pageable, number, status, movieId);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("@requiredPermission.checkPermission('UPDATE_EPISODE')")
    public ResponseEntity<String> patchEpisode(@PathVariable Integer id, @RequestBody EpisodeRequest episodeRequest) {
        return episodeService.updateEpisode(id, episodeRequest);
    }

    @DeleteMapping("/{ids}")
    @PreAuthorize("@requiredPermission.checkPermission('DELETE_EPISODE')")
    public ResponseEntity<String> deleteEpisode(@PathVariable List<Integer> ids) {
        return episodeService.deleteEpisode(ids);
    }
}
