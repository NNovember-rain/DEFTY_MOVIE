package com.defty.movie.dto.response;

import lombok.Data;

@Data
public class ShowonResponse {
    private Integer id;
    private Integer position;
    private Integer contentId;
    private String contentType;
    private String note;
}
