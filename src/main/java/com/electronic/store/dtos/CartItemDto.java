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
public class CartItemDto {

    private Integer cartItemId;

    private Product product;

    private Integer quantity;

    private Integer totalPrices;

}
