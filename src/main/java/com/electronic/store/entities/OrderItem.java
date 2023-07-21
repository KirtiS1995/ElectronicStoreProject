package com.electronic.store.entities;

import lombok.*;

import javax.persistence.*;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "orderItems")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer orderItemID;

    @Column(name = "orderItem_quantity")
    private int quantity;

    @Column(name = "orderItem_totalPrice")
    private int totalPrice;

    @ManyToOne
    @JoinColumn(name = "order_Id")
    private Order order;

    @OneToOne
    @JoinColumn(name = "productId")
    private Product product;
}
