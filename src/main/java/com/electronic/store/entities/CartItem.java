package com.electronic.store.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cart_items")
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer cartItemId;

    @OneToOne
    @JoinColumn(name = "product_id")
    private Product product;

    private Integer quantity;

    private Integer totalPrices;

    @ManyToOne
    @JoinColumn(name = "cart_id")
    private  Cart cart;

}
