package com.electronic.store.controllers;

import com.electronic.store.dtos.*;
import com.electronic.store.entities.Cart;
import com.electronic.store.entities.CartItem;
import com.electronic.store.entities.Product;
import com.electronic.store.entities.User;
import com.electronic.store.services.CartService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import com.electronic.store.dtos.CartItemDto;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CartControllerTest {

    private UserDto userDto;

    @MockBean
    private CartService cartService;

    @InjectMocks
    private CartController cartController;

    @Autowired
    private MockMvc mockMvc;

    private CartDto cartDto;
    private CartItemDto cartItemDto,  cartItemDto1;
    private ProductDto productDto,productDto1;


    @BeforeEach
    public void init(){
        userDto = UserDto.builder()
                .name("kirti")
                .email("kirti@gmail.com")
                .password("kirti123")
                .gender("female")
                .about("Testing method for create")
                .imageName("abc.png")
                .build();

        productDto = ProductDto.builder()
                .title("Samsung")
                .description("Phone having good camera")
                .price(120000)
                .discountedPrice(10000)
                .quantity(40)
                .live(true)
                .stock(false)
                .productImage("abc.png")
                .build();

        productDto1 = ProductDto.builder()
                .title("MI")
                .description("Phone having good HD camera")
                .price(20000)
                .discountedPrice(18000)
                .quantity(10)
                .live(true)
                .stock(false)
                .productImage("xyz.png")
                .build();

        cartItemDto= CartItemDto.builder()
                .product(productDto)
                .quantity(10)
                .totalPrices(13400)
                .build();

        cartItemDto1=CartItemDto.builder()
                .product(productDto1)
                .quantity(11)
                .totalPrices(15000)
                .build();


        cartDto= CartDto.builder()
                .user(userDto)
                .cartItem(Set.of(cartItemDto,cartItemDto1))
                .build();
        AddItemToCartRequest  request = AddItemToCartRequest.builder().productId("12345").quantity(10).build();


    }
    private String convertObjectToJsonString(Object user) {
        try {
            return new ObjectMapper().writeValueAsString(user);
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

       @Test
    public void addItemToCart() throws Exception {
          String userId="123";
           Mockito.when(cartService.addItemToCart(Mockito.anyString(),Mockito.any())).thenReturn(cartDto);
           //actual request for url
           this.mockMvc.perform(
                           MockMvcRequestBuilders.post("/cart/"+userId)
                                   .contentType(MediaType.APPLICATION_JSON)
                                   .content(convertObjectToJsonString(cartDto))
                                   .accept(MediaType.APPLICATION_JSON))
                   .andDo(print())
                   .andExpect(status().isOk())
                   .andExpect(jsonPath("$.user").exists());
    }

    @Test
    public void removeItemFromCart() throws Exception {
        String userId= "12345";
        Integer cartItemId=11;
        Mockito.doNothing().when(cartService).removeItemFromCart(Mockito.anyString(),Mockito.anyInt());
        this.mockMvc.perform(
                        MockMvcRequestBuilders.delete("/carts/" +userId+"/items/"+cartItemId))
                .andDo(print())
                .andExpect(status().isOk());
        //verify
        Mockito.verify(cartService,Mockito.times(1)).removeItemFromCart(userId, cartItemId);
    }

    @Test
   public void clearCart() {
        String userId= "12345";
        Integer cartItemId=11;
        Mockito.doNothing().when(cartService).removeItemFromCart(Mockito.anyString(),Mockito.anyInt());
        this.mockMvc.perform(
                        MockMvcRequestBuilders.delete("/carts/" +userId+"/items/"+cartItemId))
                .andDo(print())
                .andExpect(status().isOk());
        //verify
        Mockito.verify(cartService,Mockito.times(1)).removeItemFromCart(userId, cartItemId);
    }

    @Test
   public void getCart() {
    }
}