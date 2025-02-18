package com.defty.movie.utils;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLConnection;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UploadVideoUtil {

    @Value("${aws.s3.bucket.name}")
    private String bucketName;

    private final AmazonS3 amazonS3;

    public String upload(MultipartFile file) throws AmazonServiceException, SdkClientException, IOException {

        ObjectMetadata metadata = new ObjectMetadata();
        // Xác định MIME type cho file (Xem là mp4 hay là file khác để s3 trả về http thay vì dowload)
        String contentType = URLConnection.guessContentTypeFromName(file.getOriginalFilename());
        if (contentType == null) {
            contentType = "application/octet-stream"; // Mặc định nếu không xác định được
        }
        metadata.setContentType(contentType);
        metadata.setContentLength(file.getSize());

        String fileKey = UUID.randomUUID().toString() + file.getOriginalFilename();

        // Tạo yêu cầu upload với metadata
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, fileKey, file.getInputStream(), metadata)
                .withCannedAcl(CannedAccessControlList.PublicRead);

        amazonS3.putObject(putObjectRequest);

        // Trả về URL công khai
        return amazonS3.getUrl(bucketName, fileKey).toString();
    }
}
