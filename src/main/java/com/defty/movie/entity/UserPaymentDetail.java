package com.defty.movie.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Entity
@Setter
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserPaymentDetail extends BaseEntity {
    @Column(nullable = false)
    Integer price;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;

    @ManyToOne
    @JoinColumn(name = "membershipPackage_id")
    MembershipPackage membershipPackage;
}
