package com.defty.movie.uploadImage;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UploadImage {

    @Value("${cloudinary.url}")
    private String cloudinaryUrl;

    public String upload(List<MultipartFile> files) throws IOException {

        Cloudinary cloudinary=new Cloudinary(cloudinaryUrl);

        String linkResult="";

        for (MultipartFile file : files) {

            // Tạo tên tệp duy nhất
            String uniqueFilename = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();

            // Tải lên hình ảnh lên Cloudinary
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap(
                    "public_id", uniqueFilename,
                    "use_filename", true,
                    "unique_filename", false,
                    "overwrite", false
            ));

            linkResult+=uploadResult.get("secure_url").toString()+" ";
        }

        // Lấy URL của hình ảnh đã tải lên Cloud
        return linkResult.trim();
    }
}
