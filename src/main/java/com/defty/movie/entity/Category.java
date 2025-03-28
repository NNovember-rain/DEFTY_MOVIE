package com.defty.movie.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "category")

public class Category extends BaseEntity {
    @Column(name = "name")
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "status")
    private Integer status = 1;

//    @ManyToOne
//    @JoinColumn(name = "parent_category_id")
//    private CategoryEntity parentCategory;
//
//    @OneToMany(mappedBy = "parentCategory", cascade = CascadeType.ALL)
//    private Set<CategoryEntity> subCategories;

    @Column(name = "slug")
    private String slug;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    private Set<MovieCategory> movieCategories;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    private Set<Showon> showons;

    @ManyToMany(mappedBy = "categories")
    private Set<Collection> collections = new HashSet<>();
}
