package com.defty.movie.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

@Data
@Entity
@Table(name = "category")
public class Category extends BaseEntity {
    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

//    @ManyToOne
//    @JoinColumn(name = "parent_category_id")
//    private CategoryEntity parentCategory;
//
//    @OneToMany(mappedBy = "parentCategory", cascade = CascadeType.ALL)
//    private Set<CategoryEntity> subCategories;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    private Set<MovieCategory> movieCategories;
}
