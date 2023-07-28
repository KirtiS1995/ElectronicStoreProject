package com.electronic.store.services.impl;

import com.electronic.store.dtos.CreateOrderRequest;
import com.electronic.store.dtos.OrderDto;
import com.electronic.store.dtos.PageableResponse;
import com.electronic.store.entities.*;
import com.electronic.store.exceptions.BadApiRequestException;
import com.electronic.store.exceptions.ResourceNotFoundException;
import com.electronic.store.helper.AppConstats;
import com.electronic.store.helper.Helper;
import com.electronic.store.repositories.*;
import com.electronic.store.services.OrderService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    /**
     * @author Kirti
     * @implNote this method is for creating order
     * @param orderDto
     * @return
     */
    @Override
    public OrderDto createOrder(CreateOrderRequest orderDto) {
        logger.info("Dao Request initialized to create order ");
        String userId = orderDto.getUserId();
        String cartId = orderDto.getCartId();

        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException(AppConstats.ID_NOT_FOUND+userId));
        Cart cart = cartRepository.findById(cartId).orElseThrow(() -> new ResourceNotFoundException(AppConstats.CART_NOT_FOUND+cartId));

        //fetch CartItems
        Set<CartItem> cartitems = cart.getCartItem();
        logger.info("No. of cartItems: ",cartitems.size());
        if(cartitems.size() <= 0){
            throw new BadApiRequestException(AppConstats.INVALID_ITEMS);
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
        logger.info("Dao Request completed to create order ");
        return mapper.map(saveOrder,OrderDto.class);
    }

    /**
     *  @author Kirti
     *  @implNote this method is for removing order
     * @param orderId
     */
    @Override
    public void removeOrder(String orderId) {
        logger.info("Dao Request initialized to remove order from cart :{}",orderId);
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new ResourceNotFoundException(AppConstats.ORDER_NOT_FOUND+orderId));
        orderRepository.delete(order);
        logger.info("Dao Request completed to remove order from cart :{}",orderId);
    }

    /**
     *  @author Kirti
     *  @implNote this method is for getting all orders of user
     * @param userId
     * @return
     */
    @Override
    public List<OrderDto> getOrdersOfUser(String userId) {
        logger.info("Dao Request initialized for getting order of user :{}",userId);
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException(AppConstats.ID_NOT_FOUND+userId));

        List<Order> orderList = orderRepository.findByUser(user);

        List<OrderDto> orderDtos = orderList.stream().map(order -> mapper.map(order, OrderDto.class)).collect(Collectors.toList());
        logger.info("Dao Request completed for getting order of user :{}",userId);
        return orderDtos;
    }

    /**
     *  @author Kirti
     *  @implNote this method is for removing order
     * @param pageNumber
     * @param pageSize
     * @param sortBy
     * @param sortDir
     * @return
     */
    @Override
    public PageableResponse<OrderDto> getOrders(int pageNumber, int pageSize, String sortBy, String sortDir) {
        logger.info("Dao Request initialized for getting orders  :{}",pageNumber);
        Sort sort = (sortDir.equalsIgnoreCase("asc")) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        final Page<Order> orders = orderRepository.findAll(pageable);
        logger.info("Dao Request completed for getting orders  :{}",pageNumber);
        return Helper.getPageableResponse(orders,OrderDto.class);
    }

    /**
     * @author Kirti
     * @implNote  This method is for updating order status
     * @param orderId
     * @param orderDto
     * @return
     */
    @Override
    public OrderDto updateOrder(String orderId, OrderDto orderDto) {
        logger.info("Dao request for updating order info ith orderId :{}",orderId);
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new ResourceNotFoundException(AppConstats.ORDER_NOT_FOUND + orderId));
        order.setDeliveryDate(new Date());
        order.setOrderStatus(orderDto.getOrderStatus());
        order.setPaymentStatus(orderDto.getPaymentStatus());
        Order order1 = orderRepository.save(order);
        logger.info("Dao request completed for updating order info ith orderId :{}",orderId);
        return mapper.map(order1,OrderDto.class);
    }

}
