package com.electronic.store.services;

import com.electronic.store.dtos.CreateOrderRequest;
import com.electronic.store.dtos.OrderDto;
import com.electronic.store.entities.*;
import com.electronic.store.exceptions.ResourceNotFoundException;
import com.electronic.store.repositories.CartRepository;
import com.electronic.store.repositories.OrderRepository;
import com.electronic.store.repositories.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class OrderServiceTest {
    @MockBean
    private UserRepository userRepository;

    @MockBean
    private CartRepository cartRepository;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderService orderService;

    private User user;
    Product product1,product2;
    Cart cart;
    CartItem cartItem1,cartItem2;
    Order order,order1,order2;
    OrderItem orderItem1,orderItem2;


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

        product1 = Product.builder()
                .title("Nokia")
                .description("Phone having good battery")
                .price(40000)
                .discountedPrice(10000)
                .quantity(40)
                .live(true)
                .stock(false)
                .productImage("xyzz.png")
                .build();

        product1 = Product.builder()
                .title("MI")
                .description("Phone having good HD camera")
                .price(20000)
                .discountedPrice(18000)
                .quantity(10)
                .live(true)
                .stock(false)
                .productImage("ppp.png")
                .build();

        orderItem1=OrderItem.builder()
                .product(product1)
                .quantity(10)
                .totalPrice(134000)
                .build();

        orderItem2=OrderItem.builder()
                .product(product1)
                .quantity(5)
                .totalPrice(150000)
                .build();

        List list = new ArrayList<>();
        list.add(orderItem1);
        list.add(orderItem2);

        order= Order.builder().orderItem(list).orderAmount(3000)
                .orderDate(new Date())
                .orderStatus("Pending")
                .billingName("Kirti")
                .paymentStatus("NOT_PAID")
                .billingAddress("Pune")
                .billingPhone("78456844")
                .deliveryDate(null)
                .build();

        order1= Order.builder().orderItem(list).orderAmount(30000)
                .orderDate(new Date())
                .orderStatus("Pending")
                .billingName("sakshi")
                .paymentStatus("NOT_PAID")
                .billingAddress("A.bad")
                .billingPhone("78456844")
                .deliveryDate(null)
                .build();

        order2= Order.builder().orderItem(list).orderAmount(20000)
                .orderDate(new Date())
                .orderStatus("Pending")
                .billingName("shlok")
                .paymentStatus("NOT_PAID")
                .billingAddress("Nashik")
                .billingPhone("78456844")
                .deliveryDate(null)
                .build();
        order.setCreatedBy(user.getCreatedBy());
        order.setLastModifiedBy(user.getLastModifiedBy());
        order.setIsActive(user.getIsActive());

        List<Order> orderList = Arrays.asList(order, order1, order2);
        List<OrderDto> dtoList = orderList.stream().map(e -> mapper.map(e, OrderDto.class)).collect(Collectors.toList());

        cartItem1=CartItem.builder()
                .product(product1)
                .quantity(10)
                .totalPrices(13400)
                .build();

        cartItem2=CartItem.builder()
                .product(product2)
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
   public void createOrderTest() {
       CreateOrderRequest request=CreateOrderRequest.builder()
               .userId("user123")
               .cartId("44")
               .billingAddress("pune")
               .billingPhone("983776722")
               .billingName("kirti")
               .build();

       Mockito.when(userRepository.findById(request.getUserId())).thenReturn(Optional.of(user));
       Mockito.when(cartRepository.findById(request.getCartId())).thenReturn(Optional.of(cart));

     Mockito.doNothing().when(cartRepository.save(cart));
       Mockito.when(orderRepository.save(Mockito.any())).thenReturn(order);

       OrderDto orderDto = orderService.createOrder(request);
       System.out.println(orderDto);
       Assertions.assertNotNull(orderDto);
   }

   @Test
   public void removeOrderTest() {
        String orderId="123";
       Mockito.when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

       orderService.removeOrder(orderId);
       Mockito.verify(orderRepository,Mockito.times(1)).delete(order);
       Assertions.assertThrows(RuntimeException.class,() -> orderService.removeOrder("55"));
    }

    @Test
     public void getOrdersOfUserTest() {
        String userId= "xyz";
        List<Order> orderList = Arrays.asList(order, order1, order2);

        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        Mockito.when(orderRepository.findByUser(user)).thenReturn(orderList);
        List<OrderDto> ordersOfUser = orderService.getOrdersOfUser(userId);
        System.out.println(orderList.size());
        System.out.println(ordersOfUser.size());
        Assertions.assertEquals(orderList.size(),ordersOfUser.size());
        Assertions.assertThrows(ResourceNotFoundException.class,()->orderService.getOrdersOfUser("abc"));


    }

    @Test
    public void getOrdersTest() {
    }
}