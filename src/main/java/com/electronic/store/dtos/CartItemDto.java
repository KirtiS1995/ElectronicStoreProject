package com.electronic.store.dtos;

import com.electronic.store.entities.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CartItemDto extends BaseEntityDto {

    private Integer cartItemId;

    private ProductDto product;

    private Integer quantity;

    private Integer totalPrices;

}
