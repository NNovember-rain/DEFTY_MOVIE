package com.defty.movie.service.impl;

import com.defty.movie.dto.request.ActorRequest;
import com.defty.movie.dto.response.ActorResponse;
import com.defty.movie.dto.response.ApiResponse;
import com.defty.movie.dto.response.PageableResponse;
import com.defty.movie.entity.Actor;
import com.defty.movie.exception.ImageUploadException;
import com.defty.movie.exception.NotFoundException;
import com.defty.movie.mapper.ActorMapper;
import com.defty.movie.repository.IActorRepository;
import com.defty.movie.service.IActorService;
import com.defty.movie.utils.UploadImageUtil;
import com.defty.movie.validation.ActorValidation;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ActorService implements IActorService {
    ActorMapper actorMapper;
    ActorValidation actorValidation;
    IActorRepository actorRepository;
    UploadImageUtil uploadImageUtil;
    @Override
    public ApiResponse<Integer> addActor(ActorRequest actorRequest) {
        actorValidation.fieldValidation(actorRequest);
        Actor actorEntity = actorMapper.toActorEntity(actorRequest);
        try {
            actorEntity.setAvatar(uploadImageUtil.upload(actorRequest.getAvatar()));
        }
        catch (Exception e){
            throw new ImageUploadException("Could not upload the image, please try again later!");
        }
        try {
            actorRepository.save(actorEntity);
        }
        catch (Exception e){
            return new ApiResponse<>(500, e.getMessage(), actorEntity.getId());
        }
        return new ApiResponse<>(201, "created", actorEntity.getId());
    }

    @Override
    public ApiResponse<Integer> updateActor(Integer id, ActorRequest actorRequest) {
        actorValidation.fieldValidation(actorRequest);
        Optional<Actor> actor = actorRepository.findById(id);
        if(actor.isPresent()){
            Actor updatedActor = actor.get();

            BeanUtils.copyProperties(actorRequest, updatedActor, "id");
            try {
                updatedActor.setAvatar(uploadImageUtil.upload(actorRequest.getAvatar()));
            }
            catch (Exception e){
                throw new ImageUploadException("Could not upload the image, please try again later!");
            }
            try {
                actorRepository.save(updatedActor);
            }
            catch (Exception e){
                return new ApiResponse<>(500, e.getMessage(), id);
            }
        }
        else {
            throw new NotFoundException("Not found exception");
        }
        return new ApiResponse<>(200, "update actor successfully", id);
    }

    @Override
    public ApiResponse<List<Integer>> deleteActor(List<Integer> ids) {
        List<Actor> actors = actorRepository.findAllById(ids);
        if(actors.size() == 0) throw new NotFoundException("Not found exception");
        for(Actor actor : actors){
            actor.setStatus(-1);
        }
        actorRepository.saveAll(actors);
        if(ids.size() > 1){
            return new ApiResponse<>(200, "Delete actors successfully", ids);
        }
        return new ApiResponse<>(200, "Delete actor successfully", ids);
    }

    @Override
    public ApiResponse<Integer> changeStatus(Integer id) {
        Optional<Actor> actor = actorRepository.findById(id);
        if(actor.get() != null){
            String message = "";
            if(actor.get().getStatus() == 0){
                actor.get().setStatus(1);
                message += "Enable actors successfully";
            }
            else{
                actor.get().setStatus(0);
                message += "Disable actors successfully";
            }
            actorRepository.save(actor.get());
            return new ApiResponse<>(200, message, id);
        }
        else throw new NotFoundException("Not found exception");
    }

    @Override
    public ApiResponse<List<Integer>> disableActor(List<Integer> ids) {
        List<Actor> actors = actorRepository.findAllById(ids);
        if(actors.size() == 0) throw new NotFoundException("Not found exception");
        for(Actor actor : actors){
            actor.setStatus(0);
        }
        actorRepository.saveAll(actors);
        if(ids.size() > 1){
            return new ApiResponse<>(200, "Enable actors successfully", ids);
        }
        return new ApiResponse<>(200, "Enable actor successfully", ids);
    }

    @Override
    public ApiResponse<List<Integer>> enableActor(List<Integer> ids) {
        List<Actor> actors = actorRepository.findAllById(ids);
        if(actors.size() == 0) throw new NotFoundException("Not found exception");
        for(Actor actor : actors){
            actor.setStatus(1);
        }
        actorRepository.saveAll(actors);
        if(ids.size() > 1){
            return new ApiResponse<>(200, "Disable actors successfully", ids);
        }
        return new ApiResponse<>(200, "Disable actor successfully", ids);
    }

    @Override
    public ApiResponse<PageableResponse<ActorResponse>> getAllActors(Pageable pageable, String name, String gender, String date_of_birth, String nationality, Integer status) {
        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("createdDate").descending());

        Date startDate = null;
        Date endDate = null;
        if (date_of_birth != null && !date_of_birth.isEmpty()) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                String[] dates = date_of_birth.split(" - ");
                if (dates.length == 2) {
                    startDate = sdf.parse(dates[0]);
                    endDate = sdf.parse(dates[1]);
                    Calendar startCal = Calendar.getInstance();
                    startCal.setTime(startDate);
                    startCal.set(Calendar.HOUR_OF_DAY, 0);
                    startCal.set(Calendar.MINUTE, 0);
                    startCal.set(Calendar.SECOND, 0);
                    startCal.set(Calendar.MILLISECOND, 0);
                    startDate = startCal.getTime();

                    Calendar endCal = Calendar.getInstance();
                    endCal.setTime(endDate);
                    endCal.set(Calendar.HOUR_OF_DAY, 23);
                    endCal.set(Calendar.MINUTE, 59);
                    endCal.set(Calendar.SECOND, 59);
                    endCal.set(Calendar.MILLISECOND, 999);
                    endDate = endCal.getTime();
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        Page<Actor> actorEntities = actorRepository.findActors(
                name, gender, startDate, endDate, nationality, status, sortedPageable
        );

        List<ActorResponse> actorResponseDTOS = new ArrayList<>();
        if (actorEntities.isEmpty()){
            throw new NotFoundException("Not found exception");
        }
        else {
            for(Actor d : actorEntities){
                actorResponseDTOS.add(actorMapper.toActorResponse(d));
            }

            PageableResponse<ActorResponse> pageableResponse= new PageableResponse<>(actorResponseDTOS, actorEntities.getTotalElements());
            ApiResponse<PageableResponse<ActorResponse>> apiResponse = new ApiResponse<>(200, "OK", pageableResponse);
            return apiResponse;
        }
    }

    @Override
    public Object getActor(Integer id) {
        Optional<Actor> actorEntity = actorRepository.findById(id);
        if(actorEntity.isPresent()){
            return new ApiResponse<>(200, "OK", actorMapper.toActorResponse(actorEntity.get()));
        }
        return new ApiResponse<>(404, "Actor doesn't exist", null);
    }
}
