package com.electronic.store.services.impl;

import com.electronic.store.dtos.OrderDto;
import com.electronic.store.dtos.PageableResponse;
import com.electronic.store.repositories.*;
import com.electronic.store.services.OrderService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public OrderDto createOrder(OrderDto orderDto) {

//        String userId = orderDto.getUser().getUserId();
//        String cartId = orderDto.getCartId();
//
//        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
//        Cart cart = cartRepository.findById(cartId).orElseThrow(() -> new ResourceNotFoundException("Cart not found with id: " + cartId));
//
//        //fetch CartItems
//        List<CartItem> cartitems = cart.getItems();
//        logger.info("No. of cartItems: ",cartitems.size());
//        if(cartitems.size() <= 0){
//            throw new BadApiRequestException("Invalid no. of items in Cart!!");
//        }
//
//        // Generate Order
//        Order order = Order.builder()
//                .OrderId(UUID.randomUUID().toString())
//                .billingAddress(orderDto.getBillingAddress())
//                .billingName(orderDto.getBillingName())
//                .billingPhone(orderDto.getBillingPhone())
//                .orderedDate(new Date())
//                .deliveredDate(null)
//                .paymentStatus(orderDto.getPaymentStatus())
//                .orderStatus(orderDto.getOrderStatus())
//                .user(user)
//                .build();
//
//        // Convert CartItems to OrderItems
//
//        AtomicReference<Integer> orderAmount=new  AtomicReference<Integer>(0);
//        List<OrderItems> orderItems = cartitems.stream().map(cartitem -> {
//
//            OrderItems orderItem = OrderItems.builder()
//                    .quantity(cartitem.getQuantity())
//                    .product(cartitem.getProduct())
//                    .totalPrice(cartitem.getQuantity() * cartitem.getProduct().getDiscountedPrice())
//                    .order(order)
//                    .build();
//            orderAmount.set(orderAmount.get()+orderItem.getTotalPrice());
//            return orderItem;
//        }).collect(Collectors.toList());
//
//        order.setOrderItems(orderItems);
//        order.setOrderAmount(orderAmount.get());
//        order.setCreatedBy(orderDto.getCreatedBy());
//        order.setLastModifiedBy(orderDto.getLastModifiedBy());
//        order.setIsActive(orderDto.getIsActive());
//
//        // After converting into OrderItems, clear Cart & save it
//        cart.getItems().clear();
//        cartRepository.save(cart);
//
//        //save order
//        Order saveOrder = orderRepository.save(order);
//
//        return mapper.map(saveOrder,OrderDto.class);
        return null;
    }

    @Override
    public void removeOrder(String orderId) {

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
