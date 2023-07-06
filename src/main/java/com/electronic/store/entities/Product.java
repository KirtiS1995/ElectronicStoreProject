package com.electronic.store.entities;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "products")
@Builder
public class Product extends BaseEntity{

    @Id
    private String productId;
    private String title;
    @Column(length = 10000)
    private String description;

    private int price;
    private int discountedPrice;
    private int quantity;
    private Date addedDate;
    private boolean live;
    private boolean stock;
    private String productImage;

    @ManyToOne(fetch = FetchType.EAGER)                                     //when product load category will auto load
    @JoinColumn(name = "category_id")
    private Category category;

}



