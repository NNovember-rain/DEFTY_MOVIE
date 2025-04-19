package com.defty.movie.mapper;

import com.defty.movie.dto.request.UserRequest;
import com.defty.movie.dto.response.EpisodeCommentUserResponse;
import com.defty.movie.dto.response.UserResponse;
import com.defty.movie.entity.User;
import com.defty.movie.exception.MediaUploadException;
import com.defty.movie.utils.UploadImageUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@Slf4j
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
            String PREFIX_ACCOUNT = "ACCOUNT | ";
            log.error("{}Could not upload the image, please try again later !", PREFIX_ACCOUNT);
            throw new MediaUploadException("Could not upload the image, please try again later !");
        }
        return user;
    }

    public EpisodeCommentUserResponse toEpisodeCommentUserResponse(User user) {
        EpisodeCommentUserResponse episodeCommentUserResponse = new EpisodeCommentUserResponse();
        BeanUtils.copyProperties(user, episodeCommentUserResponse);
        episodeCommentUserResponse.setAvatar(user.getAvatar());
        return episodeCommentUserResponse;
    }
}
