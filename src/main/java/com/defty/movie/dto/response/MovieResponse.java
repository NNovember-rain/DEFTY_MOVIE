package com.defty.movie.dto.response;

import com.defty.movie.entity.Movie;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MovieResponse {
    Integer id;

    String title;

    String description;

    String trailer;

    String thumbnail;

    String coverImage;

    Integer status;

    String nation;

    Date releaseDate;

    Integer membershipType;

    Integer ranking;

    String slug;

    String director;

    Date createdDate;

    String createdBy;

    Date modifiedDate;

    String modifiedBy;

    public static MovieResponse fromEntity(Movie movie) {
        MovieResponse response = new MovieResponse();
        response.setId(movie.getId());
        response.setTitle(movie.getTitle());
        response.setNation(movie.getNation());
        response.setReleaseDate(movie.getReleaseDate());
        response.setRanking(movie.getRanking());
        return response;
    }
}
