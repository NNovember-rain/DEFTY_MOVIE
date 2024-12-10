package com.defty.movie.mapper;

import com.defty.movie.dto.request.UserRequest;
import com.defty.movie.dto.response.UserResponse;
import com.defty.movie.entity.User;
import com.defty.movie.exception.ImageUploadException;
import com.defty.movie.utils.UploadImageUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserMapper {
    ModelMapper modelMapper;
    PasswordEncoder passwordEncoder;
    UploadImageUtil uploadImageUtil;

    public UserResponse toUserResponse(User user) {
        return modelMapper.map(user, UserResponse.class);
    }

    public User toUser(UserRequest userRequest) {
        User user = modelMapper.map(userRequest, User.class);
        user.setStatus(1);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        try {
            user.setAvatar(uploadImageUtil.upload(userRequest.getAvatar()));
        }catch (Exception e){
            throw new ImageUploadException("Could not upload the image, please try again later !");
        }
        return user;
    }
}
