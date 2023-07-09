package com.electronic.store.dtos;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BaseEntityDto {

//            @NotBlank
            public String createdBy;

            private LocalDateTime createdOn;

//            @NotBlank
            private String lastModifiedBy;

            private LocalDateTime modifiedOn;

//            @NotBlank
            private String isActive;
}
