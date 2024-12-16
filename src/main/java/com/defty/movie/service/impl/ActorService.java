package com.defty.movie.service.impl;

import com.defty.movie.dto.request.ActorRequest;
import com.defty.movie.dto.response.ActorResponse;
import com.defty.movie.dto.response.PageableResponse;
import com.defty.movie.entity.Actor;
import com.defty.movie.entity.Director;
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
    public ResponseEntity<String> addActor(ActorRequest actorRequest) {
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
            e.printStackTrace();
        }
        return ResponseEntity.ok("Add actor successfully");
    }

    @Override
    public ResponseEntity<String> updateActor(Integer id, ActorRequest actorRequest) {
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
                e.printStackTrace();
            }
        }
        else {
            throw new NotFoundException("Not found exception");
        }
        return ResponseEntity.ok("Update actor successfully");
    }

    @Override
    public ResponseEntity<String> deleteActor(List<Integer> ids) {
        return null;
    }

    @Override
    public PageableResponse<ActorResponse> getAllActors(Pageable pageable, String name, String gender, String date_of_birth, String nationality, Integer status) {
        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("createdDate").descending());

        Date sqlDate_of_birth = null;
        if (date_of_birth != null && !date_of_birth.isEmpty()) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                java.util.Date utilDate = sdf.parse(date_of_birth);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(utilDate);
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);

                sqlDate_of_birth = calendar.getTime();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        Page<Actor> actorEntities = actorRepository.findActors(name, gender, sqlDate_of_birth, nationality, status, sortedPageable);
        List<ActorResponse> actorResponseDTOS = new ArrayList<>();
        if (actorEntities.isEmpty()){
            throw new NotFoundException("Not found exception");
        }
        else {
            for(Actor d : actorEntities){
                actorResponseDTOS.add(actorMapper.toActorResponse(d));
            }

            PageableResponse<ActorResponse> pageableResponse= new PageableResponse<>(actorResponseDTOS, actorRepository.count());
            return pageableResponse;
        }
    }

    @Override
    public Object getActor(Integer id) {
        Optional<Actor> actorEntity = actorRepository.findById(id);
        if(actorEntity.isPresent()){
            return actorMapper.toActorResponse(actorEntity.get());
        }
        return "Actor doesn't exist";
    }
}
