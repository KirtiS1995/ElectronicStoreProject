package com.electronic.store.services.impl;

import com.electronic.store.dtos.CreateOrderRequest;
import com.electronic.store.dtos.OrderDto;
import com.electronic.store.dtos.PageableResponse;
import com.electronic.store.entities.*;
import com.electronic.store.exceptions.BadApiRequestException;
import com.electronic.store.exceptions.ResourceNotFoundException;
import com.electronic.store.helper.AppConstats;
import com.electronic.store.repositories.*;
import com.electronic.store.services.OrderService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ModelMapper mapper;

    private static Logger logger= LoggerFactory.getLogger(OrderServiceImpl.class);


    @Override
    public OrderDto createOrder(CreateOrderRequest orderDto) {
        String userId = orderDto.getUserId();
        String cartId = orderDto.getCartId();

        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        Cart cart = cartRepository.findById(cartId).orElseThrow(() -> new ResourceNotFoundException("Cart not found with id: " + cartId));

        //fetch CartItems
        Set<CartItem> cartitems = cart.getCartItem();
        logger.info("No. of cartItems: ",cartitems.size());
        if(cartitems.size() <= 0){
            throw new BadApiRequestException("Invalid no. of items in Cart!!");
        }

        // Generate Order
        Order order = Order.builder()
                .orderId(UUID.randomUUID().toString())
                .billingAddress(orderDto.getBillingAddress())
                .billingName(orderDto.getBillingName())
                .billingPhone(orderDto.getBillingPhone())
                .orderDate(new Date())
                .deliveryDate(null)
                .paymentStatus(orderDto.getPaymentStatus())
                .orderStatus(orderDto.getOrderStatus())
                .user(user)
                .build();

        // Convert CartItems to OrderItems

        AtomicReference<Integer> orderAmount=new  AtomicReference<Integer>(0);
        List<OrderItem> orderItems = cartitems.stream().map(cartitem -> {

            OrderItem orderItem = OrderItem.builder()
                    .quantity(cartitem.getQuantity())
                    .product(cartitem.getProduct())
                    .totalPrice(cartitem.getQuantity() * cartitem.getProduct().getDiscountedPrice())
                    .order(order)
                    .build();
            orderAmount.set(orderAmount.get()+orderItem.getTotalPrice());
            return orderItem;
        }).collect(Collectors.toList());

        order.setOrderItem(orderItems);
        order.setOrderAmount(orderAmount.get());
        order.setCreatedBy(orderDto.getCreatedBy());
        order.setLastModifiedBy(orderDto.getLastModifiedBy());
        order.setIsActive(orderDto.getIsActive());

        // After converting into OrderItems, clear Cart & save it
        cart.getCartItem().clear();
        cartRepository.save(cart);

        //save order
        Order saveOrder = orderRepository.save(order);

        return mapper.map(saveOrder,OrderDto.class);
    }

    @Override
    public void removeOrder(String orderId) {

        Order order = orderRepository.findById(orderId).orElseThrow(() -> new ResourceNotFoundException("Order is not found with id: " + orderId));
        orderRepository.delete(order);
    }

    @Override
    public List<OrderDto> getOrdersOfUser(String userId) {
        return null;
    }

    @Override
    public PageableResponse<OrderDto> getOrders(int pageNo, int pageSize, String sortBy, String sortDir) {
        return null;
    }
}
