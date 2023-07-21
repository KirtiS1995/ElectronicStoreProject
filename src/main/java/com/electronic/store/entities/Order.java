package com.electronic.store.entities;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "orders")
public class Order extends BaseEntity{


}
