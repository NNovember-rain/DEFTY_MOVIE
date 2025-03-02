package com.defty.movie.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Where;

import java.util.List;
import java.util.Set;

@Data
@Entity
@Table(name = "category")

public class Category extends BaseEntity {
    @Column(name = "name")
    private String name;

    @Column(name = "description")
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
}
