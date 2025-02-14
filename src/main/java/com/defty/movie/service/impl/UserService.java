package com.defty.movie.service.impl;
import com.defty.movie.dto.response.UserResponse;
import com.defty.movie.dto.response.ApiResponse;
import com.defty.movie.dto.response.PageableResponse;
import com.defty.movie.entity.User;
import com.defty.movie.entity.User;
import com.defty.movie.exception.CustomDateException;
import com.defty.movie.exception.NotFoundException;
import com.defty.movie.mapper.UserMapper;
import com.defty.movie.repository.IUserRepository;
import com.defty.movie.service.IUserService;
import com.defty.movie.utils.DateUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService implements IUserService {
    IUserRepository userRepository;
    UserMapper userMapper;
    DateUtil dateUtil;
    @Override
    public ApiResponse<PageableResponse<UserResponse>> getAllUsers(Pageable pageable, String name, String gender, String date_of_birth, String nationality, Integer status) {
        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("createdDate").descending());
        Date startDate = null;
        Date endDate = null;
        if (date_of_birth != null && !date_of_birth.isEmpty()) {
            try{
                String[] dates = date_of_birth.split(" - ");
                if (dates.length == 2) {
                    startDate = dateUtil.stringToSqlDate(dates[0]);
                    endDate = dateUtil.stringToSqlDate(dates[1]);
                }
                else{
                    throw new CustomDateException("please enter the right date format: dd/MM/yyyy - dd/MM/yyyy");
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
        Page<User> users = userRepository.findUsers(
                name, gender, startDate, endDate, status, sortedPageable
        );

        List<UserResponse> userResponseDTOS = new ArrayList<>();
        if (users.isEmpty()){
            throw new NotFoundException("Not found exception");
        }
        else {
            for(User m : users){
                userResponseDTOS.add(userMapper.toUserResponse(m));
            }
            PageableResponse<UserResponse> pageableResponse = new PageableResponse<>(userResponseDTOS, users.getTotalElements());
            return new ApiResponse<>(200, "OK", pageableResponse);
        }
    }

    @Override
    public ApiResponse<Integer> changeStatus(Integer id) {
        Optional<User> user = userRepository.findById(id);
        if(user.get() != null){
            String message = "";
            if(user.get().getStatus() == 0){
                user.get().setStatus(1);
                message += "Enable users successfully";
            }
            else{
                user.get().setStatus(0);
                message += "Disable users successfully";
            }
            userRepository.save(user.get());
            return new ApiResponse<>(200, message, id);
        }
        else throw new NotFoundException("Not found exception");
    }
    @Override
    public Object getUser(Integer id) {
        Optional<User> userEntity = userRepository.findById(id);
        if(userEntity.isPresent()){
            return new ApiResponse<>(200, "OK", userMapper.toUserResponse(userEntity.get()));
        }
        return new ApiResponse<>(404, "User doesn't exist", null);
    }
}
