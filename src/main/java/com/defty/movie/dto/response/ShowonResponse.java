package com.defty.movie.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class ShowonResponse {
    private Integer id;
    private Integer position;
    private Integer contentId;
    private String contentType;
    private String contentName;
    private Integer status;
    private String note;
    private Object contentItems;
}
