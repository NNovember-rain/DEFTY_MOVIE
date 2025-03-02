package com.defty.movie.dto.request;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class  DirectorRequest {
    String fullName;

    String gender;

    String dateOfBirth;

    Integer weight;

    Integer height;

    Integer orderIndex;

    String nationality;

    String description;

    MultipartFile avatar;
}
