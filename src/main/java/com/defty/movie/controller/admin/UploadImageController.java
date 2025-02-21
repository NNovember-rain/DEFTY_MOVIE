package com.defty.movie.controller.admin;

import com.defty.movie.exception.ImageUploadException;
import com.defty.movie.utils.ApiResponeUtil;
import com.defty.movie.utils.UploadImageUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("${api.prefix}/admin/upload-image")
public class UploadImageController {

    UploadImageUtil uploadImageUtil;

    @PostMapping
    public Object uploadImage(@RequestParam("image") MultipartFile image) {
        if(image!=null) {
            try {
                String imageLink=uploadImageUtil.upload(image);
                return ApiResponeUtil.ResponseCreatedSuccess(imageLink);
            } catch (Exception e) {
                e.printStackTrace();
                throw new ImageUploadException("Could not upload the image, please try again later !");
            }
        }else throw new ImageUploadException("Could not upload the image, please choose an image !");
    }
}
