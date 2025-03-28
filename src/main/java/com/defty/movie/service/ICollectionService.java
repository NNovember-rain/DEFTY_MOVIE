package com.defty.movie.service;

import com.defty.movie.dto.request.CollectionRequest;
import com.defty.movie.dto.response.ApiResponse;
import com.defty.movie.dto.response.CollectionResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ICollectionService {
    ApiResponse<Object> addItemsToCollection(Integer collectionId, List<Integer> itemIds, String type);
    ApiResponse<Object> removeItemsFromCollection(Integer collectionId, List<Integer> itemIds, String type);
    ApiResponse<Object> addCollection(CollectionRequest collectionRequest);
    ApiResponse<Object> updateCollection(Integer id, CollectionRequest collectionRequest);
    ApiResponse<Object> deleteCollection(List<Integer> ids);
    Object getAllCollections(Pageable pageable, String name, Integer status);
    Object getCollection(Integer id);
    ApiResponse<Object> changeStatus(Integer id);
    ApiResponse<Object> getContentInCollection(Integer collectionId);
    ApiResponse<Object> getContentNotInCollection(Integer collectionId);
}