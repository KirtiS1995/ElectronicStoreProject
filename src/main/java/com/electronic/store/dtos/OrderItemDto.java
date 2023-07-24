package com.electronic.store.dtos;

import com.electronic.store.entities.Product;

import javax.validation.constraints.NotBlank;

public class OrderItemDto {
    private Integer orderItemId;

    @NotBlank(message = "OrderItem quantity is required .")
    private int quantity;

    private int totalPrize;

    private Product product;
}
