package com.electronic.store.entities;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@MappedSuperclass
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BaseEntity {


    @Column(name = "created_by", nullable = false)
    @CreatedBy
    public String createdBy;

    @Column(name = "created_date", updatable = false)
    @CreationTimestamp
    private LocalDateTime createdOn;

    @Column(name = "updated_by", nullable = false)
    @LastModifiedBy
    private String lastModifiedBy;

    @Column(name = "modified_date", updatable = false)
    @UpdateTimestamp
    private LocalDateTime modifiedOn;

    @Column(name = "is_active")
    private String isActive;


}