package com.defty.movie.entity;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.persistence.*;
import lombok.Data;
@Data
@Entity
@Table(name = "banner",uniqueConstraints = @UniqueConstraint(columnNames = {"contentType", "contentId"}))
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Banner extends BaseEntity{
    @Column(name = "`key`")
    private String key;
    @Column(name = "thumnail")
    private String thumnail;
    @Column(name = "title")
    private String title;
    @Column(name = "link")
    private String link;
    @Column(name = "position")
    private Integer position;
    @Column(name = "status")
    private Integer status;
    @Column(name = "contentType")
    private String contentType;
    @Column(name = "contentId")
    private Integer contentId;
}
