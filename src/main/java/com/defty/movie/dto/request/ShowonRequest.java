package com.defty.movie.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class ShowonRequest {
    private Integer position;
//    private String positionKey;
//    private String positionName;
    private Integer contentId;
    private String contentType;
    private String note;
//    private Long partnerId;
}
