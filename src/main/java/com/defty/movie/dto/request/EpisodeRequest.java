package com.defty.movie.dto.request;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EpisodeRequest {
    Integer id;
    Integer number;
    String description;
    MultipartFile thumbnail;
    String link;
    Integer movieId;
}
