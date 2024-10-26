package com.defty.movie.service.impl;

import com.defty.movie.dto.request.DirectorRequest;
import com.defty.movie.dto.response.DirectorResponse;
import com.defty.movie.dto.response.PageableResponse;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DirectorService implements IDirectorService {
    DirectorMapper directorMapper;
    DirectorValidation directorValidation;
    IDirectorRepository directorRepository;
    UploadImageUtil uploadImageUtil;
    @Override
    public ResponseEntity<String> addDirector(DirectorRequest directorRequest) {
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
            e.printStackTrace();
        }
        return ResponseEntity.ok("Add director successfully");
    }

    @Override
    public ResponseEntity<String> updateDirector(Integer id, DirectorRequest directorRequest) {
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
        return ResponseEntity.ok("Update director successfully");
    }

    @Override
    public ResponseEntity<String> deleteDirector(List<Integer> ids) {
        return null;
    }

    @Override
    public PageableResponse<DirectorResponse> getAllDirectors(Pageable pageable) {
        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("createdDate").descending());
        Page<Director> directorEntities = directorRepository.findAll(pageable);
        List<DirectorResponse> directorResponseDTOS = new ArrayList<>();
        if (directorEntities.isEmpty()){
            throw new NotFoundException("Not found exception");
        }
        else {
            for(Director d : directorEntities){
                directorResponseDTOS.add(directorMapper.toDirectorResponseDTO(d));
            }

            PageableResponse<DirectorResponse> pageableResponse= new PageableResponse<>(directorResponseDTOS, directorRepository.count());
            return pageableResponse;
        }
    }

    @Override
    public Object getDirector(Integer id) {
        Optional<Director> directorEntity = directorRepository.findById(id);
        if(directorEntity.isPresent()){
            return directorMapper.toDirectorResponseDTO(directorEntity.get());
        }
        return "Director doesn't exist";
    }
}
