package com.defty.movie.service.impl;
import com.defty.movie.dto.response.UserResponse;
import com.defty.movie.dto.response.ApiResponse;
import com.defty.movie.dto.response.PageableResponse;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService implements IUserService {
    private final IUserRepository userRepository;
    private final UserMapper userMapper;
    DateUtil dateUtil;
//    @Override
//    public ApiResponse<PageableResponse<UserResponse>> getAllUsers(Pageable pageable, String name, String gender, String date_of_birth, String nationality, Integer status) {
//        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("createdDate").descending());
//
//        Date startDate = null;
//        Date endDate = null;
//        if (date_of_birth != null && !date_of_birth.isEmpty()) {
//            try {
//                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
//                String[] dates = date_of_birth.split(" - ");
//                if (dates.length == 2) {
//                    startDate = sdf.parse(dates[0]);
//                    endDate = sdf.parse(dates[1]);
//                    Calendar startCal = Calendar.getInstance();
//                    startCal.setTime(startDate);
//                    startCal.set(Calendar.HOUR_OF_DAY, 0);
//                    startCal.set(Calendar.MINUTE, 0);
//                    startCal.set(Calendar.SECOND, 0);
//                    startCal.set(Calendar.MILLISECOND, 0);
//                    startDate = startCal.getTime();
//
//                    Calendar endCal = Calendar.getInstance();
//                    endCal.setTime(endDate);
//                    endCal.set(Calendar.HOUR_OF_DAY, 23);
//                    endCal.set(Calendar.MINUTE, 59);
//                    endCal.set(Calendar.SECOND, 59);
//                    endCal.set(Calendar.MILLISECOND, 999);
//                    endDate = endCal.getTime();
//                }
//                else {
//
//                }
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//        }
//        Page<User> userEntities = userRepository.findUsers(
//                name, gender, startDate, endDate, status, sortedPageable
//        );
//
//        List<UserResponse> userResponseDTOS = new ArrayList<>();
//        if (userEntities.isEmpty()){
//            throw new NotFoundException("Not found exception");
//        }
//        else {
//            for(User d : userEntities){
//                userResponseDTOS.add(userMapper.toUserResponse(d));
//            }
//
//            PageableResponse<UserResponse> pageableResponse= new PageableResponse<>(userResponseDTOS, userEntities.getTotalElements());
//            ApiResponse<PageableResponse<UserResponse>> apiResponse = new ApiResponse<>(200, "OK", pageableResponse);
//            return apiResponse;
//        }
//    }
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

}
