package com.defty.movie.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "director")
public class Director extends BaseEntity{
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

    @Column(name = "nationality")
    private String nationality;

    @Column(name="positon")
    private Integer position;

    @Column(name = "description")
    private String description;

    @Column(name = "status")
    private Integer status = 1;

    @Column(name = "avatar")
    private String avatar;

    @OneToMany(mappedBy = "director", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Movie> movies;
}
