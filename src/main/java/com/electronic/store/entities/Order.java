package com.electronic.store.entities;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "orders")
public class Order extends BaseEntity{

    @Id
    private String orderId;
    @Column(name = "order_status")
    private String orderStatus;
    @Column(name = "payment_status")
    private String paymentStatus;
    @Column(name = "order_amount")
    private int orderAmount;
    @Column(name = "billing_address")
    private String billingAddress;
    @Column(name = "billing_phone")
    private  String billingPhone;
    @Column(name = "billing_name")
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
