package com.electronic.store.services;

import com.electronic.store.dtos.OrderDto;
import com.electronic.store.dtos.PageableResponse;

import java.util.List;

public interface OrderService {

    OrderDto createOrder(OrderDto orderDto);

    void removeOrder(String orderId);

    List<OrderDto> getOrdersOfUser(String userId);

    PageableResponse<OrderDto> getOrders(int pageNo, int pageSize, String sortBy, String sortDir);

    }
