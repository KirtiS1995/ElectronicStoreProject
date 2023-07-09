package com.electronic.store.dtos;


import lombok.*;

import javax.persistence.Column;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryDto extends BaseEntityDto
{
    private String categoryId;

    @NotBlank
    @Size(max = 20,message = "Enter valid title..")
    private String title;

    @Size(min = 10,message = "Enter Description upto range.!")
    private String description;

    @NotBlank
    private String coverImage;



}
