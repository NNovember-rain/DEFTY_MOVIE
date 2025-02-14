package com.defty.movie.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "actor")
public class Actor extends BaseEntity{
    @Column(name = "full_name")
    private String fullName;

    @Column(name = "gender")
    private String gender;

    @Column(name = "date_of_birth")
    private Date dateOfBirth;

    @Column(name = "weight")
    private Integer weight;

    @Column(name = "height")
    private Integer height;

    @Column(name = "status")
    private Integer status = 1;

    @Column(name = "nationality")
    private String nationality;

    @Column(name = "avatar")
    private String avatar;

    @Column(name = "description")
    private String description;

    @ManyToMany(mappedBy = "actors")
    private Set<Movie> movies;
}
