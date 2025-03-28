package com.defty.movie.controller.admin;

import com.defty.movie.dto.request.CollectionRequest;
import com.defty.movie.dto.response.ApiResponse;
import com.defty.movie.service.ICollectionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/admin/collection")
public class CollectionController {
    private final ICollectionService collectionService;
    @PostMapping("")
    @PreAuthorize("@requiredPermission.checkPermission('CREATE_COLLECTION')")
    public ApiResponse<Object> addCollection(@RequestBody CollectionRequest collectionRequest) {
        return collectionService.addCollection(collectionRequest);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("@requiredPermission.checkPermission('UPDATE_COLLECTION')")
    public ApiResponse<Object> patchCollection(@PathVariable Integer id, @RequestBody CollectionRequest collectionRequest) {
        return collectionService.updateCollection(id, collectionRequest);
    }

    @DeleteMapping("/{ids}")
    @PreAuthorize("@requiredPermission.checkPermission('DELETE_COLLECTION')")
    public ApiResponse<Object> deleteCollection(@PathVariable List<Integer> ids) {
        return collectionService.deleteCollection(ids);
    }

    @GetMapping("/all")
    @PreAuthorize("@requiredPermission.checkPermission('GET_COLLECTIONS')")
    public Object getCollections(Pageable pageable,
                                 @RequestParam(name = "name", required = false) String name,
                                 @RequestParam(name = "status", required = false) Integer status) {
        return collectionService.getAllCollections(pageable, name, status);
    }

    @GetMapping("/{id}")
    @PreAuthorize("@requiredPermission.checkPermission('GET_COLLECTION')")
    public Object getCollection(@PathVariable Integer id) {
        return collectionService.getCollection(id);
    }

    @PatchMapping("/status/{id}")
    @PreAuthorize("@requiredPermission.checkPermission('CHANGE_COLLECTION_STATUS')")
    public ApiResponse<Object> changeStatus(@PathVariable Integer id) {
        return collectionService.changeStatus(id);
    }
    @PatchMapping("/{collectionId}/add")
    @PreAuthorize("@requiredPermission.checkPermission('ADD_ITEM_TO_COLLECTION')")
    public ApiResponse<Object> addItemsToCollection(@PathVariable Integer collectionId,
                                                    @RequestParam String type,
                                                    @RequestParam List<Integer> itemIds) {
        return collectionService.addItemsToCollection(collectionId, itemIds, type);
    }

    @DeleteMapping("/{collectionId}/remove")
    @PreAuthorize("@requiredPermission.checkPermission('REMOVE_ITEM_FROM_COLLECTION')")
    public ApiResponse<Object> removeItemsFromCollection(@PathVariable Integer collectionId,
                                                         @RequestParam String type,
                                                         @RequestParam List<Integer> itemIds) {
        return collectionService.removeItemsFromCollection(collectionId, itemIds, type);
    }
    @GetMapping("/{collectionId}/contents")
    @PreAuthorize("@requiredPermission.checkPermission('GET_CONTENT_IN_COLLECTION')")
    public ApiResponse<Object> getContentInCollection(@PathVariable Integer collectionId) {
        return collectionService.getContentInCollection(collectionId);
    }
    @GetMapping("/{collectionId}/content-not-in")
    @PreAuthorize("@requiredPermission.checkPermission('GET_CONTENT_NOT_IN_COLLECTION')")
    public ApiResponse<Object> getContentNotInCollection(@PathVariable Integer collectionId) {
        return collectionService.getContentNotInCollection(collectionId);
    }

}
