package com.electronic.store.dtos;

import com.electronic.store.entities.OrderItem;
import com.electronic.store.entities.User;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class OrderDto extends BaseEntityDto{

    private String orderId;
    private String orderStatus="PENDING";
    private String paymentStatus="NOTPAID";
    private int orderAmount;

    @NotBlank(message = "Billing address required..!")
    private String billingAddress;

    @NotBlank(message = "Billing Phone required..!")
    private  String billingPhone;

    @NotBlank(message = "Billing Name required..!")
    private String billingName;

    private Date orderDate= new Date();
    private Date deliveryDate;

//    private UserDto user;

    private List<OrderItemDto> orderItem = new ArrayList<>();

}
