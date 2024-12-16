package com.defty.movie.controller.admin;

import com.defty.movie.dto.request.DirectorRequest;
import com.defty.movie.service.impl.DirectorService;
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
@RequestMapping("${api.prefix}/admin/movie/director")
//@RequestMapping("/movie/director")
public class DirectorController {
    private final DirectorService directorService;
    @PostMapping("")
    @PreAuthorize("@requiredPermission.checkPermission('CREATE_DIRECTOR')")
    public ResponseEntity<String> addDirector(@ModelAttribute DirectorRequest directorRequest){
        return directorService.addDirector(directorRequest);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("@requiredPermission.checkPermission('UPDATE_DIRECTOR')")
    public ResponseEntity<String> patchDirector(@PathVariable Integer id, @ModelAttribute DirectorRequest directorRequest) {
        return directorService.updateDirector(id, directorRequest);
    }

    @GetMapping("/all")
    @PreAuthorize("@requiredPermission.checkPermission('GET_DIRECTORS')")
    public  Object getDirectors(Pageable pageable,
                                @RequestParam(name = "name", required = false) String name,
                                @RequestParam(name = "gender", required = false) String gender,
                                @RequestParam(name = "date_of_birth", required = false) String date_of_birth,
                                @RequestParam(name = "nationality", required = false) String nationality,
                                @RequestParam(name = "status", required = false) Integer status) {
        return directorService.getAllDirectors(pageable, name, gender, date_of_birth, nationality, status);
    }

    @GetMapping("/{id}")
    @PreAuthorize("@requiredPermission.checkPermission('GET_DIRECTOR')")
    public Object getDirector(@PathVariable Integer id){
        return directorService.getDirector(id);
    }

    @DeleteMapping("/{ids}")
    @PreAuthorize("@requiredPermission.checkPermission('DELETE_DIRECTOR')")
    public ResponseEntity<String> deleteDirector(@PathVariable List<Integer> ids) {
        return directorService.deleteDirector(ids);
    }
}
