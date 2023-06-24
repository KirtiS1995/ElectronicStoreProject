package com.electronic.store.dtos;

import com.electronic.store.entities.Category;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ProductDto extends BaseEntityDto {

    private String productId;
    @NotBlank
    private String title;

    @NotBlank
    @Size(max = 100,message = "Description should be  within range ")
    private String description;

    @NotBlank(message = "Price should not be blank")
    private int price;

    @NotBlank(message = "Discount price should not be blank ")
    private int discountedPrice;

    @NotBlank
    private int quantity;

    private Date addedDate;

    private boolean live;
    private boolean stock;

    private String productImage;
    private CategoryDto category;

}
