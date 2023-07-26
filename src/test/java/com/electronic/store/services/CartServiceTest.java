package com.electronic.store.services;

import com.electronic.store.dtos.AddItemToCartRequest;
import com.electronic.store.dtos.CartDto;
import com.electronic.store.entities.Cart;
import com.electronic.store.entities.CartItem;
import com.electronic.store.entities.Product;
import com.electronic.store.entities.User;
import com.electronic.store.repositories.CartItemRepository;
import com.electronic.store.repositories.CartRepository;
import com.electronic.store.repositories.ProductRepository;
import com.electronic.store.repositories.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@SpringBootTest
public class CartServiceTest {

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private CartRepository cartRepository;

    @MockBean
    private CartItemRepository cartItemRepository;

    @MockBean
    private ProductRepository productRepository;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private CartService cartService;

    User user;
    Cart cart;
    Product product,product1;
    CartItem cartItem1,cartItem2;


    @BeforeEach
    public void init(){
        user = User.builder()
                .name("kirti")
                .email("kirti@gmail.com")
                .password("kirti123")
                .gender("female")
                .about("Testing method for create")
                .imageName("abc.png")
                .build();

        product = Product.builder()
                .title("Samsung")
                .description("Phone having good camera")
                .price(120000)
                .discountedPrice(10000)
                .quantity(40)
                .live(true)
                .stock(false)
                .productImage("abc.png")
                .build();

        product1 = Product.builder()
                .title("MI")
                .description("Phone having good HD camera")
                .price(20000)
                .discountedPrice(18000)
                .quantity(10)
                .live(true)
                .stock(false)
                .productImage("xyz.png")
                .build();

        cartItem1=CartItem.builder()
         .product(product)
         .quantity(10)
         .totalPrices(13400)
         .build();

        cartItem2=CartItem.builder()
                .product(product1)
                .quantity(11)
                .totalPrices(15000)
                .build();

        cart= Cart.builder()
                .user(user)
                .cartItem(Set.of(cartItem1,cartItem2))
                .build();
        cart.setCreatedBy(user.getCreatedBy());
        cart.setLastModifiedBy(user.getLastModifiedBy());
        cart.setIsActive(user.getIsActive());
    }

    @Test
    public void addItemToCartTest() {
        String productId = "12345";
        String userId = "user123";

        AddItemToCartRequest request=new AddItemToCartRequest();
        request.setProductId(productId);
        request.setQuantity(10);

        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        Mockito.when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        Mockito.when(cartRepository.save(Mockito.any())).thenReturn(cart);

        CartDto cartDto = cartService.addItemToCart(userId, request);
        Assertions.assertNotNull(cartDto);
        System.out.println(cartDto.getUser().getEmail());
        Assertions.assertEquals(cartDto.getUser().getName(),cart.getUser().getName());

    }
    @Test
    public void removeItemFromCartTest() {
        String userId = "123user";
        Integer cartItemId=55;

        Mockito.when(cartItemRepository.findById(cartItemId)).thenReturn(Optional.of(cartItem1));
        cartService.removeItemFromCart(userId,cartItemId);
        Mockito.verify(cartItemRepository,Mockito.times(1)).delete(cartItem1);
        Assertions.assertThrows(RuntimeException.class,() -> cartService.removeItemFromCart("xyz",5));

    }
    @Test
    public void clearCartTest() {
        String userId = "xyz";
        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        Mockito.when(cartRepository.findByUser(Mockito.any())).thenReturn(Optional.of(cart));
        cartRepository.delete(cart);
//        cartService.clearCart(userId);
        Mockito.when(cartRepository.save(cart)).thenReturn(cart);
        Assertions.assertThrows(RuntimeException.class,() -> cartService.clearCart("abc"));
    }

    @Test
    public void getCartByUSerTest() {
        String userId = "123";
        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        Mockito.when(cartRepository.findByUser(Mockito.any())).thenReturn(Optional.of(cart));

        CartDto cartByUSer = cartService.getCartByUSer(userId);
        System.out.println(cartByUSer.getUser().getName());
        System.out.println(cartByUSer);
        Assertions.assertNotNull(cartByUSer);
        Assertions.assertEquals(cart.getUser().getName(),cartByUSer.getUser().getName());
        Assertions.assertThrows(RuntimeException.class,() -> cartService.getCartByUSer("abc"));

    }

}
