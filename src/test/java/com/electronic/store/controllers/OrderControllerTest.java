package com.electronic.store.controllers;

import com.electronic.store.dtos.*;
import com.electronic.store.services.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.*;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @InjectMocks
    private OrderController orderController;

    private CreateOrderRequest request;
    private UserDto userDto;
    private OrderDto orderDto,orderDto1,orderDto2;
    private OrderItemDto orderItemDto,orderItemDto1;
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
                .title("Nokia")
                .description("Phone having good battery")
                .price(40000)
                .discountedPrice(10000)
                .quantity(40)
                .live(true)
                .stock(false)
                .productImage("xyzz.png")
                .build();

        productDto1 = ProductDto.builder()
                .title("MI")
                .description("Phone having good HD camera")
                .price(20000)
                .discountedPrice(18000)
                .quantity(10)
                .live(true)
                .stock(false)
                .productImage("ppp.png")
                .build();
        orderItemDto= OrderItemDto.builder()
                .product(productDto)
                .totalPrice(134000)
                .quantity(10)
                .build();

        orderItemDto1=OrderItemDto.builder()
                .product(productDto1)
                .quantity(5)
                .totalPrice(150000)
                .build();

        List list=new ArrayList(Set.of(orderItemDto,orderItemDto1));
        orderDto= OrderDto.builder().orderItem(list).orderAmount(3000)
                .orderDate(new Date())
                .orderStatus("Pending")
                .billingName("Kirti")
                .paymentStatus("NOT_PAID")
                .billingAddress("Pune")
                .billingPhone("78456844")
                .deliveryDate(null)
                .build();

        orderDto= OrderDto.builder().orderItem(list).orderAmount(30000)
                .orderDate(new Date())
                .orderStatus("Pending")
                .billingName("sakshi")
                .paymentStatus("NOT_PAID")
                .billingAddress("A.bad")
                .billingPhone("78456844")
                .deliveryDate(null)
                .build();

        orderDto= OrderDto.builder().orderItem(list).orderAmount(20000)
                .orderDate(new Date())
                .orderStatus("Pending")
                .billingName("shlok")
                .paymentStatus("NOT_PAID")
                .billingAddress("Nashik")
                .billingPhone("78456844")
                .deliveryDate(null)
                .build();

        orderDto.setCreatedBy(userDto.getCreatedBy());
        orderDto.setLastModifiedBy(userDto.getLastModifiedBy());
        orderDto.setIsActive(userDto.getIsActive());
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
   public void createOrderTest() throws Exception {
        request = CreateOrderRequest.builder()
                .billingAddress("Pune")
                .cartId("123")
                .userId("user123").
                billingPhone("789545121")
                .billingName("Kirti")
                .build();

        Mockito.when(orderService.createOrder(Mockito.any())).thenReturn(orderDto);
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/orders/")
                        .contentType(MediaType.APPLICATION_JSON).
                        content(convertObjectToJsonString(request))
                        .accept(MediaType.APPLICATION_JSON)).andDo(
                                print()).
                andExpect(status().isCreated()).andExpect(jsonPath("$.billingPhone").exists());
    }

    @Test
  public void removeOrderTest() {
        String orderId="12345";
        orderService.removeOrder(orderId);
        Mockito.verify(orderService,Mockito.times(1)).removeOrder(orderId);
    }

    @Test
   public void getOrdersOfUserTest() throws Exception {
        String userId="abc";
        Mockito.when(orderService.getOrdersOfUser(userId)).thenReturn(Arrays.asList(orderDto,orderDto1,orderDto2));
        //actual request for url
        this.mockMvc.perform(
                        MockMvcRequestBuilders.get("/orders/user/" +userId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void getOrdersTest() throws Exception {
        PageableResponse pageableResponse=new PageableResponse<>();
        pageableResponse.setContent(Arrays.asList(orderDto,orderDto1,orderDto2));

        pageableResponse.setLastPage(false);
        pageableResponse.setTotalElements(100);
        pageableResponse.setTotalPages(20);
        pageableResponse.setPageSize(10);
        pageableResponse.setPageNumber(1);

        Mockito.when(orderService.getOrders(Mockito.anyInt(),Mockito.anyInt(),Mockito.anyString(),Mockito.anyString()))
                .thenReturn(pageableResponse);

        this.mockMvc.perform(
                MockMvcRequestBuilders.get("/orders/users")
                        .contentType(MediaType.APPLICATION_JSON).
                        accept(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isOk()).andReturn().getResponse();
    }
//    @Test
//    public  void updateOrderTest() throws Exception {
//
//        String orderId="1";
//        Mockito.when(orderService.updateOrder(Mockito.any(),Mockito.anyString())).thenReturn(orderDto);
//
//        mockMvc.perform(put("/orders/update/"+orderId)
//                        //.header(HttpHeaders.AUTHORIZATION,"")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(convertObjectToJsonString(orderDto))
//                        .accept(MediaType.APPLICATION_JSON)).andDo(print())
//                .andExpect(status().isOk()).andExpect(jsonPath("$.billingAddress").exists());
//    }
}