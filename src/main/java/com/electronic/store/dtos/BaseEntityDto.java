package com.electronic.store.dtos;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
@MappedSuperclass
@Setter
@Getter
public class BaseEntityDto {

            @NotBlank
            @Size(min = 5,max = 10,message = "Enter valid name  ..")
             public String createdBy;

             private LocalDateTime createdOn;

             @NotBlank
             @Size(min = 5,max = 10,message = "Enter valid name  ..")
             private String lastModifiedBy;

             private LocalDateTime modifiedOn;

             private String isActive;
}
