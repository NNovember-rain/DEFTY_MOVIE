package com.defty.movie.service.impl;

import com.defty.movie.dto.request.CollectionRequest;
import com.defty.movie.dto.response.*;
import com.defty.movie.entity.*;
import com.defty.movie.exception.NotFoundException;
import com.defty.movie.mapper.CollectionMapper;
import com.defty.movie.repository.*;
import com.defty.movie.service.ICollectionService;
import com.defty.movie.utils.CopyUtil;
import com.defty.movie.utils.SlugUtil;
import com.defty.movie.validation.CollectionValidation;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CollectionService implements ICollectionService {
    ICollectionRepository collectionRepository;
    IMovieRepository movieRepository;
    IDirectorRepository directorRepository;
    IActorRepository actorRepository;
    ICategoryRepository categoryRepository;
    CollectionValidation collectionValidation;
    CollectionMapper collectionMapper;
    SlugUtil slugUtil;


    @Override
    public ApiResponse<Object> addCollection(CollectionRequest collectionRequest) {
        collectionValidation.fieldValidation(collectionRequest);
        Collection collectionEntity = collectionMapper.toCollectionEntity(collectionRequest);
        try {
            Collection newCollection = collectionRepository.save(collectionEntity);
            newCollection.setSlug(slugUtil.createSlug(newCollection.getName(), newCollection.getId()));
            collectionRepository.save(newCollection);
        }
        catch (Exception e){
            return new ApiResponse<>(500, e.getMessage(), collectionEntity.getId());
        }
        return new ApiResponse<>(201, "created", collectionEntity.getId());
    }

    @Override
    public ApiResponse<Object> updateCollection(Integer id, CollectionRequest collectionRequest) {
        collectionValidation.fieldValidation(collectionRequest);
        Optional<Collection> collectionEntity = collectionRepository.findById(id);
        if(collectionEntity.isPresent()){
            Collection updatedCollection = collectionEntity.get();
            CopyUtil.copyPropertiesIgnoreNull(collectionRequest, updatedCollection);
            try {
                updatedCollection.setSlug(slugUtil.createSlug(collectionRequest.getName(), id));
                collectionRepository.save(updatedCollection);
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
        else{
            throw new NotFoundException("Not found exception");

        }
        return new ApiResponse<>(200, "Update collection successfully", id);
    }

    @Override
    public ApiResponse<Object> deleteCollection(List<Integer> ids) {
        List<Collection> collections = collectionRepository.findAllById(ids);
        if(collections.size() == 0) throw new NotFoundException("Not found exception");
        for(Collection c : collections){
            c.setStatus(-1);
        }
        collectionRepository.saveAll(collections);
        if(ids.size() > 1){
            return new ApiResponse<>(200, "Delete collections successfully", ids);
        }
        return new ApiResponse<>(200, "Delete collection successfully", ids);
    }

    @Override
    public Object getAllCollections(Pageable pageable, String name, Integer status) {
        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("createdDate").descending());
        Page<Collection> collections = collectionRepository.findCollections(name, status, sortedPageable);
        List<CollectionResponse> collectionResponses = new ArrayList<>();
        if (collections.isEmpty()){
            throw new NotFoundException("Not found exception");
        }
        else {
            for(Collection c : collections){
                collectionResponses.add(collectionMapper.toCollectionResponse(c));
            }

            PageableResponse<CollectionResponse> pageableResponse= new PageableResponse<>(collectionResponses, collections.getTotalElements());
            return new ApiResponse<>(200, "OK", pageableResponse);
        }
    }

    @Override
    public Object getCollection(Integer id) {
        Optional<Collection> collection = collectionRepository.findById(id);
        if(collection.isPresent()){
            return new ApiResponse<>(200, "OK", collectionMapper.toCollectionResponse(collection.get()));
        }
        return new ApiResponse<>(404, "Collection doesn't exist", null);
    }

    @Override
    public ApiResponse<Object> changeStatus(Integer id) {
        Optional<Collection> collection = collectionRepository.findById(id);
        if(collection.get() != null){
            String message = "";
            if(collection.get().getStatus() == 0){
                collection.get().setStatus(1);
                message += "Enable collections successfully";
            }
            else {
                collection.get().setStatus(0);
                message += "Disable collections successfully";
            }
            collectionRepository.save(collection.get());
            return new ApiResponse<>(200, message, id);
        }
        else throw new NotFoundException("Not found exception");
    }


    @Override
    public ApiResponse<Object> addItemsToCollection(Integer collectionId, List<Integer> itemIds, String type) {
        Optional<Collection> optionalCollection = collectionRepository.findById(collectionId);
        if (optionalCollection.isEmpty()) {
            throw new NotFoundException("Collection doesn't exist");
        }
        Collection collection = optionalCollection.get();

        switch (type) {
            case "movie":
                List<Movie> movies = movieRepository.findAllById(itemIds);
                collection.getMovies().addAll(movies);
                break;
            case "director":
                List<Director> directors = directorRepository.findAllById(itemIds);
                collection.getDirectors().addAll(directors);
                break;
            case "actor":
                List<Actor> actors = actorRepository.findAllById(itemIds);
                collection.getActors().addAll(actors);
                break;
            case "category":
                List<Category> categories = categoryRepository.findAllById(itemIds);
                collection.getCategories().addAll(categories);
                break;
            default:
                return new ApiResponse<>(400,"Invalid type", collectionId);
        }

        collectionRepository.save(collection);

        return new ApiResponse<>(200, "add " + type + " to collection successfully", itemIds);
    }

    @Override
    public ApiResponse<Object> removeItemsFromCollection(Integer collectionId, List<Integer> itemIds, String type) {
        Optional<Collection> optionalCollection = collectionRepository.findById(collectionId);
        if (optionalCollection.isEmpty()) {
            throw new NotFoundException("Collection doesn't exist");
        }
        Collection collection = optionalCollection.get();

        switch (type) {
            case "movie":
                collection.getMovies().removeIf(movie -> itemIds.contains(movie.getId()));
                break;
            case "director":
                collection.getDirectors().removeIf(director -> itemIds.contains(director.getId()));
                break;
            case "actor":
                collection.getActors().removeIf(actor -> itemIds.contains(actor.getId()));
                break;
            case "category":
                collection.getCategories().removeIf(category -> itemIds.contains(category.getId()));
                break;
            default:
                return new ApiResponse<>(400,"Invalid type", collectionId);
        }

        collectionRepository.save(collection);
        return new ApiResponse<>(200, "delete " + type + " from category successfully", itemIds);
    }

    @Override
    public ApiResponse<Object> getContentInCollection(Integer collectionId) {
        Optional<Collection> optionalCollection = collectionRepository.findById(collectionId);
        if (optionalCollection.isEmpty()) {
            throw new NotFoundException("Collection doesn't exist");
        }

        Collection collection = optionalCollection.get();

        CollectionResponse response = collectionMapper.toCollectionResponse(collection);

        response.setCategories(collection.getCategories().stream().map(CategoryResponse::fromEntity).toList());
        response.setMovies(collection.getMovies().stream().map(MovieResponse::fromEntity).toList());
        response.setDirectors(collection.getDirectors().stream().map(DirectorResponse::fromEntity).toList());
        response.setActors(collection.getActors().stream().map(ActorResponse::fromEntity).toList());
        return new ApiResponse<>(200, "OK", response);
    }

    @Override
    public ApiResponse<Object> getContentNotInCollection(Integer collectionId) {
        Optional<Collection> optionalCollection = collectionRepository.findById(collectionId);
        if (optionalCollection.isEmpty()) {
            throw new NotFoundException("Collection doesn't exist");
        }

        Collection collection = optionalCollection.get();


        List<Category> allCategories = categoryRepository.findAll();
        List<Movie> allMovies = movieRepository.findAll();
        List<Director> allDirectors = directorRepository.findAll();
        List<Actor> allActors = actorRepository.findAll();


        List<CategoryResponse> categories = allCategories.stream()
                .filter(category -> !collection.getCategories().contains(category))
                .map(CategoryResponse::fromEntity)
                .toList();

        List<MovieResponse> movies = allMovies.stream()
                .filter(movie -> !collection.getMovies().contains(movie))
                .map(MovieResponse::fromEntity)
                .toList();

        List<DirectorResponse> directors = allDirectors.stream()
                .filter(director -> !collection.getDirectors().contains(director))
                .map(DirectorResponse::fromEntity)
                .toList();

        List<ActorResponse> actors = allActors.stream()
                .filter(actor -> !collection.getActors().contains(actor))
                .map(ActorResponse::fromEntity)
                .toList();

        CollectionResponse response = collectionMapper.toCollectionResponse(collection);
        response.setCategories(categories);
        response.setMovies(movies);
        response.setDirectors(directors);
        response.setActors(actors);

        return new ApiResponse<>(200, "OK", response);
    }



}
