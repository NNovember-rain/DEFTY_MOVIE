package com.defty.movie.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Data;
@Data
@Entity
@Table(name = "banner")
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
    @Transient
    private String contentType;
    @Transient
    private Integer contentId;
}
