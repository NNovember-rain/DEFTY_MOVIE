package com.defty.movie.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.*;
import jakarta.persistence.MappedSuperclass;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Setter
@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BaseEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = -863164858986274318L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

//    @Column(name = "createddate")
//    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss.SSS")
//    @CreatedDate
//    private Date createdDate;
//
//    @Column(name = "createdby")
//    @CreatedBy
//    private String createdBy;
//
//    @Column(name = "modifieddate")
//    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss.SSS")
//    @LastModifiedDate
//    private Date modifiedDate;
//
//    @Column(name = "modifiedby")
//    @LastModifiedBy
//    private String modifiedBy;
}