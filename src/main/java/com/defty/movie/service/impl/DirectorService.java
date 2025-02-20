package com.defty.movie.service.impl;

import com.defty.movie.dto.request.DirectorRequest;
import com.defty.movie.dto.response.*;
import com.defty.movie.entity.Director;
import com.defty.movie.exception.CustomDateException;
import com.defty.movie.exception.ImageUploadException;
import com.defty.movie.exception.NotFoundException;
import com.defty.movie.mapper.DirectorMapper;
import com.defty.movie.repository.IDirectorRepository;
import com.defty.movie.service.IDirectorService;
import com.defty.movie.utils.DateUtil;
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
    DateUtil dateUtil;
    @Override
    public ApiResponse<Integer> addDirector(DirectorRequest directorRequest) {
        directorValidation.fieldValidation(directorRequest);
        Director directorEntity = directorMapper.toDirectorEntity(directorRequest);
        directorEntity.setDateOfBirth(dateUtil.stringToSqlDate(directorRequest.getDateOfBirth()));
        if (directorRequest.getAvatar() != null && !directorRequest.getAvatar().isEmpty()) {
            try {
                directorEntity.setAvatar(uploadImageUtil.upload(directorRequest.getAvatar()));
            } catch (Exception e) {
                throw new ImageUploadException("Could not upload the image, please try again later!");
            }
        }
        else{
            directorEntity.setAvatar(null);
        }
        try {
            directorRepository.save(directorEntity);
        } catch (Exception e) {
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
            if (directorRequest.getAvatar() != null && !directorRequest.getAvatar().isEmpty()) {
                try {
                    updatedDirector.setAvatar(uploadImageUtil.upload(directorRequest.getAvatar()));
                } catch (Exception e) {
                    throw new ImageUploadException("Could not upload the image, please try again later!");
                }
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
        Date startDate = null;
        Date endDate = null;
        if (date_of_birth != null && !date_of_birth.isEmpty()) {
            String[] dates = date_of_birth.split(" - ");
            if (dates.length == 2) {
                startDate = dateUtil.stringToSqlDate(dates[0]);
                endDate = dateUtil.stringToSqlDate(dates[1]);
            }
            else{
                throw new CustomDateException("please enter the right date format: dd/MM/yyyy - dd/MM/yyyy");
            }
        }

        Page<Director> directorEntities = directorRepository.findDirectors(name, gender, startDate, endDate, nationality, status, sortedPageable);
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
    public PageableResponse<DirectorResponse> getAllDirectorOrder(Pageable pageable) {

        List<Director> directors = directorRepository.findAll(pageable).getContent();
        List<Director> directorsSize=directorRepository.findAll();
        if(directors.size() == 0) throw new NotFoundException("Not found exception");
        PageableResponse<DirectorResponse> directorResponsePageableResponse =new PageableResponse<>();
        List<DirectorResponse> directorResponseDTOS = new ArrayList<>();
        for(Director director : directors){
            directorResponseDTOS.add(directorMapper.toDirectorResponseDTO(director));
        }
        directorResponsePageableResponse.setContent(directorResponseDTOS);
        directorResponsePageableResponse.setTotalElements(directorsSize.size()+0L);
        return directorResponsePageableResponse;
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
