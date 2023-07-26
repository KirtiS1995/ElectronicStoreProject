package com.electronic.store.dtos;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddItemToCartRequest {

    private String productId;

    private Integer quantity;


}
