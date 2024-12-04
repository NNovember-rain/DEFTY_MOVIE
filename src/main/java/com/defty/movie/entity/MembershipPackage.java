package com.defty.movie.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Entity
@Setter
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MembershipPackage extends BaseEntity {
    @Column(nullable = false)
    String name;

    @Column
    Integer price;

    @Column
    Integer discount;

    @Column
    Integer duration;

    @Column
    Integer status;

    @Column
    String description;

    @OneToMany(mappedBy = "membershipPackage")
    Set<UserPaymentDetail> userPaymentDetails;
}
