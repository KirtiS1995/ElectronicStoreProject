package com.electronic.store.dtos;

import com.electronic.store.entities.CartItem;
import com.electronic.store.entities.User;
import lombok.*;

import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.util.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartDto extends BaseEntityDto {

    private String cartId;

    private Date createdAt;

    private UserDto user;

    private Set<CartItemDto> cartItem = new HashSet<>();

}


