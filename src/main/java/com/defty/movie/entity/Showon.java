package com.defty.movie.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "show_on")
public class Showon extends BaseEntity{
    @Column(name = "position")
    private Integer position;

    @Column(name = "content_type")
    private String contentType;

    @Column(name = "note")
    private String note;

    @Column(name = "status")
    private Integer status = 1;

    @ManyToOne
    @JoinColumn(name = "content_id")
    private Category category;
}
