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
public class OrderDto extends BaseEntityDto{

    private String orderId;
    private String orderStatus="PENDING";
    private String paymentStatus="NOTPAID";
    private int orderAmount;

    @NotBlank(message = "Billing address required..!")
    private String billingAddress;
    private  String billingPhone;
    private String billingName;

    private Date orderDate;
    private Date deliveryDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_Id")
    private User user;

    @OneToMany(mappedBy = "order",fetch = FetchType.EAGER,cascade = CascadeType.REMOVE)
    @JoinColumn(name = "order_orderItems")
    private List<OrderItem> orderItem = new ArrayList<OrderItem>();

}
