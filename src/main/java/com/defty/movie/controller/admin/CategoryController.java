package com.defty.movie.controller.admin;

import com.defty.movie.dto.request.CategoryRequest;
import com.defty.movie.service.impl.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
//@RequestMapping("${api.prefix}/movie")
@RequestMapping("/movie/category")
public class CategoryController {
    private final CategoryService  categoryService;
    @PostMapping("")
    public ResponseEntity<String> addCategory(@RequestBody CategoryRequest  categoryRequest){
        return  categoryService.addCategory( categoryRequest);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<String> patchCategory(@PathVariable Integer id, @RequestBody CategoryRequest  categoryRequest) {
        return  categoryService.updateCategory(id,  categoryRequest);
    }

    @GetMapping("/all")
    public  Object getCategorys(Pageable pageable) {
        return  categoryService.getAllCategorys(pageable);
    }

    @GetMapping("/{id}")
    public Object getCategory(@PathVariable Integer id){
        return  categoryService.getCategory(id);
    }
}
