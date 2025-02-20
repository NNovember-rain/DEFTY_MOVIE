package com.defty.movie.service.impl;

import com.defty.movie.dto.request.ShowonRequest;
import com.defty.movie.dto.response.ShowonResponse;
import com.defty.movie.dto.response.ApiResponse;
import com.defty.movie.dto.response.PageableResponse;
import com.defty.movie.dto.response.ShowonResponse;
import com.defty.movie.entity.Category;
import com.defty.movie.entity.Showon;
import com.defty.movie.exception.NotFoundException;
import com.defty.movie.mapper.ShowonMapper;
import com.defty.movie.repository.ICategoryRepository;
import com.defty.movie.repository.IShowonRepository;
import com.defty.movie.service.IShowonService;
import com.defty.movie.validation.ShowonValidation;
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
public class ShowonService implements IShowonService {
    ShowonValidation showonValidation;
    ShowonMapper showonMapper;
    IShowonRepository showonRepository;
    ICategoryRepository categoryRepository;
    @Override
    public ApiResponse<Integer> addShowon(ShowonRequest showonRequest) {
        showonValidation.fieldValidation(showonRequest);
        Showon showon = showonMapper.toShowonEntity(showonRequest);
        if (showonRequest.getContentType().equals("category")){
            Optional<Category> category = categoryRepository.findById(showonRequest.getContentId());
            showon.setCategory(category.get());
        }
        try{
            showonRepository.save(showon);
        }
        catch (Exception e){
            return new ApiResponse<>(500, e.getMessage(), showon.getId());
        }
        return new ApiResponse<>(201, "created", showon.getId());
    }

    @Override
    public ApiResponse<Integer> updateShowon(Integer id, ShowonRequest showonRequest) {
        showonValidation.fieldValidation(showonRequest);
        Optional<Showon> showon = showonRepository.findById(id);
        if(showon.isPresent()){
            Showon updatedShowon = showon.get();
            BeanUtils.copyProperties(showonRequest, updatedShowon, "id");
            if (showonRequest.getContentType().equals("category")){
                Optional<Category> category = categoryRepository.findById(showonRequest.getContentId());
                updatedShowon.setCategory(category.get());
            }
            try {
                showonRepository.save(updatedShowon);
            }catch (Exception e){
                return new ApiResponse<>(500, e.getMessage(), updatedShowon.getId());
            }
        }
        else{
            throw new NotFoundException("Not found exception");
        }
        return new ApiResponse<>(201, "update show on successfully", id);
    }

    @Override
    public ApiResponse<List<Integer>> deleteShowon(List<Integer> ids) {
        List<Showon> showons = showonRepository.findAllById(ids);
        if(showons.size() == 0) throw new NotFoundException("Not found exception");
        for(Showon showon : showons){
            showon.setStatus(-1);
        }
        showonRepository.saveAll(showons);
        if(ids.size() > 1){
            return new ApiResponse<>(200, "Delete showons successfully", ids);
        }
        return new ApiResponse<>(200, "Delete showon successfully", ids);
    }

    @Override
    public ApiResponse<Integer> changeStatus(Integer id) {
        Optional<Showon> showon = showonRepository.findById(id);
        if(showon.get() != null){
            String message = "";
            if(showon.get().getStatus() == 0){
                showon.get().setStatus(1);
                message += "Enable showon successfully";
            }
            else{
                showon.get().setStatus(0);
                message += "Disable showon successfully";
            }
            showonRepository.save(showon.get());
            return new ApiResponse<>(200, message, id);
        }
        else throw new NotFoundException("Not found exception");
    }

    @Override
    public ApiResponse<PageableResponse<ShowonResponse>> getAllShowons(Pageable pageable, String contentType, String contentName, Integer status) {
        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("createdDate").descending());
        Page<Showon> showons = showonRepository.getShowons(contentType, contentName, status, sortedPageable);

        List<ShowonResponse> showonResponses = new ArrayList<>();
        if(showons.isEmpty()){
            throw new NotFoundException("Not found exception");
        }
        else{
            for (Showon s : showons){
                 showonResponses.add(showonMapper.toShowonResponse(s));
            }
            PageableResponse<ShowonResponse> pageableResponse= new PageableResponse<>(showonResponses, showons.getTotalElements());
            ApiResponse<PageableResponse<ShowonResponse>> apiResponse = new ApiResponse<>(200, "OK", pageableResponse);
            return apiResponse;
        }
    }

    @Override
    public Object getShowon(Integer id) {
        Optional<Showon> showon = showonRepository.findById(id);
        if (showon.isPresent()){
            ShowonResponse showonResponse = showonMapper.toShowonResponse(showon.get());
            return new ApiResponse<>(200, "OK", showonResponse);
        }
        return new ApiResponse<>(404, "Showon doesn't exist", null);
    }
}
