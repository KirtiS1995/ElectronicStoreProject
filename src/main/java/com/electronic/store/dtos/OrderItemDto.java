package com.electronic.store.dtos;

import lombok.*;

import javax.validation.constraints.NotBlank;


@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
@Builder
public class OrderItemDto {
    private Integer orderItemId;

    @NotBlank(message = "OrderItem quantity is required .")
    private int quantity;

    private int totalPrice;

    private ProductDto product;
}
