package com.defty.movie.entity;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)

public class Permission extends BaseEntity {
    @Column(nullable = false, length = 255)
    String api;

    @Column(length = 255)
    String description;

    @OneToMany(mappedBy = "permission", fetch = FetchType.LAZY)
    Set<RolePermission> rolePermissions;

}
