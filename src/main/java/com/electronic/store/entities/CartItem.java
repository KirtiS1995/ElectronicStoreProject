package com.electronic.store.entities;

import lombok.*;

import javax.persistence.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "cart_items")
public class CartItem {

    @OneToOne
    @JoinColumn(name = "product_id")
    private Product product;

    private Integer quantity;

    private Integer totalPrices;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer cartItemId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id")
    private  Cart cart;

}
