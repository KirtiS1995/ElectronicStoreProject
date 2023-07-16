package com.electronic.store.services;

import com.electronic.store.dtos.AddItemToCartRequest;
import com.electronic.store.dtos.CartDto;

public interface CartService {
    //add items to cart
    //case 1: cart for user is not available,We will create the cart,
    //Case 2 : cart available add items to cart

    CartDto addItemToCart(String userId, AddItemToCartRequest request);

    //remove items from cart
    void removeItemFromCart(String userId,int cartItem);
=======
    void removeItemToCart(String userId,int cartItem);

    //remove all items from cart
    void clearCart(String userId);

    //getting cart by user
    CartDto getCartByUSer(String userId);
}
