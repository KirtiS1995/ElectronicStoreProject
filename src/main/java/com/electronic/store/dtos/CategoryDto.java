package com.electronic.store.dtos;


import lombok.*;

import javax.persistence.Column;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryDto extends BaseEntityDto
{
    private String categoryId;

    @NotBlank
//    @Min(value = 4,message = "Title must be minimum 4 characters !!")
//    @Max(value = 10,message = "Title must be Maximum 10 characters")
    private String title;

    @NotBlank(message = "Description is required..!")
    private String description;

    @NotBlank
    private String coverImage;


}
