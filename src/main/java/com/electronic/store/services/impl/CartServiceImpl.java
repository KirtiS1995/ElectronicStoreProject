package com.electronic.store.services.impl;

import com.electronic.store.dtos.AddItemToCartRequest;
import com.electronic.store.dtos.CartDto;
import com.electronic.store.entities.Cart;
import com.electronic.store.entities.CartItem;
import com.electronic.store.entities.Product;
import com.electronic.store.entities.User;
import com.electronic.store.exceptions.BadApiRequestException;
import com.electronic.store.exceptions.ResourceNotFoundException;
import com.electronic.store.helper.AppConstats;
import com.electronic.store.repositories.CartItemRepository;
import com.electronic.store.repositories.CartRepository;
import com.electronic.store.repositories.ProductRepository;
import com.electronic.store.repositories.UserRepository;
import com.electronic.store.services.CartService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ModelMapper mapper;

    @Override
    public CartDto addItemToCart(String userId, AddItemToCartRequest request) {
        int quantity = request.getQuantity();
        String productId = request.getProductId();

        if(quantity<=0)
        {
            throw new BadApiRequestException(AppConstats.QUANTITY_NOT_VALID);
        }
    //Fetch product
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException(AppConstats.PRODUCT_NOT_FOUND));

        //fetch USer
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException(AppConstats.ID_NOT_FOUND));

        Cart cart = null;
        try {
            cart = cartRepository.findByUser(user).get();
        }catch (NoSuchElementException e)
        {
            cart = new Cart();
            cart.setCartId(UUID.randomUUID().toString());
            cart.setCreatedAt(new Date());
        }
        //perform cart operation
        //If cart item already present,then update
        AtomicReference<Boolean> updated= new AtomicReference<>(false);
        List<CartItem> items = cart.getCartItem();
        List<CartItem> updatedItems = items.stream().map(item ->
        {
            if (item.getProduct().getProductId().equals(productId)) {
                //item already present in cart
                item.setQuantity(quantity);
                item.setTotalPrices(quantity*product.getPrice());
                updated.set(true);
            }
            return item;
        }).collect(Collectors.toList());

        cart.setCartItem(updatedItems);

    if (!updated.get()){
            //create items
            CartItem cartItem = CartItem.builder()
                    .quantity(quantity)
                    .totalPrices(quantity * product.getDiscountedPrice())
                    .cart(cart)
                    .product(product)
                    .build();
    cart.getCartItem().add(cartItem);
}
        cart.setUser(user);
        Cart updatedCart = cartRepository.save(cart);
        return mapper.map(updatedCart,CartDto.class);
    }

    @Override
    public void removeItemFromCart(String userId, int cartItem) {
        CartItem cartItem1 = cartItemRepository.findById(cartItem).orElseThrow(() -> new ResourceNotFoundException(AppConstats.CART_NOT_FOUND));
        cartItemRepository.delete(cartItem1);
    }

    @Override
    public void clearCart(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException(AppConstats.ID_NOT_FOUND));
        Cart cart = cartRepository.findByUser(user).orElseThrow(() -> new ResourceNotFoundException(AppConstats.CART_NOT_FOUND));
        cart.getCartItem().clear();
        cartRepository.save(cart);
    }

    @Override
    public CartDto getCartByUSer(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException(AppConstats.ID_NOT_FOUND));
        Cart cart = cartRepository.findByUser(user).orElseThrow(() -> new ResourceNotFoundException(AppConstats.CART_NOT_FOUND));
        return mapper.map(cart,CartDto.class);
    }
}
