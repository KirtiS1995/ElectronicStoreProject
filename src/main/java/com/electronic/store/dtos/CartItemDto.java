package com.electronic.store.dtos;

import com.electronic.store.entities.Product;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartItemDto {

    private Integer cartItemId;

    private ProductDto product;

    private Integer quantity;

    private Integer totalPrices;

}
