package com.defty.movie.controller.admin;

import com.defty.movie.dto.request.CategoryRequest;
import com.defty.movie.dto.response.ApiResponse;
import com.defty.movie.service.ICategoryService;
import com.defty.movie.service.impl.CategoryService;
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
@RequestMapping("${api.prefix}/admin/movie/category")
//@RequestMapping("/movie/category")
public class CategoryController {
    private final ICategoryService categoryService;
    @PostMapping("")
    @PreAuthorize("@requiredPermission.checkPermission('CREATE_CATEGORY')")
    public ApiResponse<Integer> addCategory(@RequestBody CategoryRequest  categoryRequest){
        return  categoryService.addCategory( categoryRequest);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("@requiredPermission.checkPermission('UPDATE_CATEGORY')")
    public ApiResponse<Integer> patchCategory(@PathVariable Integer id, @RequestBody CategoryRequest  categoryRequest) {
        return  categoryService.updateCategory(id,  categoryRequest);
    }

    @DeleteMapping("/{ids}")
    @PreAuthorize("@requiredPermission.checkPermission('DELETE_CATEGORY')")
    public ApiResponse<List<Integer>> deleteCategory(@PathVariable List<Integer> ids) {
        return categoryService.deleteCategory(ids);
    }

    @GetMapping("/all")
    @PreAuthorize("@requiredPermission.checkPermission('GET_CATEGORIES')")
    public Object getCategorys(Pageable pageable,
                                @RequestParam(name = "name", required = false) String name,
                                @RequestParam(name = "status", required = false) Integer status) {
        return categoryService.getAllCategories(pageable, name, status);
    }

    @GetMapping("/{id}")
    @PreAuthorize("@requiredPermission.checkPermission('GET_CATEGORY')")
    public Object getCategory(@PathVariable Integer id){
        return  categoryService.getCategory(id);
    }


    @PatchMapping("/status/{id}")
    @PreAuthorize("@requiredPermission.checkPermission('CHANGE_CATEGORY_STATUS')")
    public ApiResponse<Integer> changeStatus(@PathVariable Integer id) {
        return categoryService.changeStatus(id);
    }
}
