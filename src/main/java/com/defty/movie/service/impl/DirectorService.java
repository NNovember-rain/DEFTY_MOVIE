package com.defty.movie.service.impl;

import com.defty.movie.dto.request.DirectorRequest;
import com.defty.movie.dto.response.ApiResponse;
import com.defty.movie.dto.response.CategoryResponse;
import com.defty.movie.dto.response.DirectorResponse;
import com.defty.movie.dto.response.PageableResponse;
import com.defty.movie.entity.Actor;
import com.defty.movie.entity.Director;
import com.defty.movie.exception.ImageUploadException;
import com.defty.movie.exception.NotFoundException;
import com.defty.movie.mapper.DirectorMapper;
import com.defty.movie.repository.IDirectorRepository;
import com.defty.movie.service.IDirectorService;
import com.defty.movie.utils.UploadImageUtil;
import com.defty.movie.validation.DirectorValidation;
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
public class DirectorService implements IDirectorService {
    DirectorMapper directorMapper;
    DirectorValidation directorValidation;
    IDirectorRepository directorRepository;
    UploadImageUtil uploadImageUtil;
    @Override
    public ApiResponse<Integer> addDirector(DirectorRequest directorRequest) {
        directorValidation.fieldValidation(directorRequest);
        Director directorEntity = directorMapper.toDirectorEntity(directorRequest);
        try {
            directorEntity.setAvatar(uploadImageUtil.upload(directorRequest.getAvatar()));
        }
        catch (Exception e){
            throw new ImageUploadException("Could not upload the image, please try again later!");
        }
        try {
            directorRepository.save(directorEntity);
        }
        catch (Exception e){
            return new ApiResponse<>(500, e.getMessage(), directorEntity.getId());
        }
        return new ApiResponse<>(201, "Created", directorEntity.getId());
    }

    @Override
    public ApiResponse<Integer> updateDirector(Integer id, DirectorRequest directorRequest) {
        directorValidation.fieldValidation(directorRequest);
        Optional<Director> director = directorRepository.findById(id);
        if(director.isPresent()){
            Director updatedDirector = director.get();

            BeanUtils.copyProperties(directorRequest, updatedDirector, "id");
            try {
                updatedDirector.setAvatar(uploadImageUtil.upload(directorRequest.getAvatar()));
            }
            catch (Exception e){
                throw new ImageUploadException("Could not upload the image, please try again later!");
            }
            try {
                directorRepository.save(updatedDirector);
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
        else {
            throw new NotFoundException("Not found exception");
        }
        return new ApiResponse<>(200, "Update director successfully", id);
    }

    @Override
    public ApiResponse<PageableResponse<DirectorResponse>> getAllDirectors(Pageable pageable, String name, String gender, String date_of_birth, String nationality, Integer status) {
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

        Page<Director> directorEntities = directorRepository.findDirectors(name, gender, sqlDate_of_birth, nationality, status, sortedPageable);
        List<DirectorResponse> directorResponseDTOS = new ArrayList<>();
        if (directorEntities.isEmpty()){
            throw new NotFoundException("Not found exception");
        }
        else {
            for(Director d : directorEntities){
                directorResponseDTOS.add(directorMapper.toDirectorResponseDTO(d));
            }

            PageableResponse<DirectorResponse> pageableResponse= new PageableResponse<>(directorResponseDTOS, directorEntities.getTotalElements());
            return new ApiResponse<>(200, "OK", pageableResponse);
        }
    }

    @Override
    public Object getDirector(Integer id) {
        Optional<Director> directorEntity = directorRepository.findById(id);
        if(directorEntity.isPresent()){
            return new ApiResponse<>(200, "OK", directorMapper.toDirectorResponseDTO(directorEntity.get()));
        }
        return new ApiResponse<>(404, "Director doesn't exist", null);
    }

    @Override
    public ApiResponse<List<Integer>> deleteDirector(List<Integer> ids) {
        List<Director> directors = directorRepository.findAllById(ids);
        if(directors.size() == 0) throw new NotFoundException("Not found exception");
        for(Director director : directors){
            director.setStatus(-1);
        }
        directorRepository.saveAll(directors);
        if(ids.size() > 1){
            return new ApiResponse<>(200, "Delete directors successfully", ids);
        }
        return new ApiResponse<>(200, "Delete director successfully", ids);
    }
    @Override
    public ApiResponse<Integer> changeStatus(Integer id) {
        Optional<Director> director = directorRepository.findById(id);
        if(director.get() != null){
            String message = "";
            if(director.get().getStatus() == 0){
                director.get().setStatus(1);
                message += "Enable directors successfully";
            }
            else{
                director.get().setStatus(0);
                message += "Disable directors successfully";
            }
            directorRepository.save(director.get());
            return new ApiResponse<>(200, message, id);
        }
        else throw new NotFoundException("Not found exception");
    }
}
