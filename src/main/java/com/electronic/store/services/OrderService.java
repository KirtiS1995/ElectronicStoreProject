package com.electronic.store.services;

import com.electronic.store.dtos.CreateOrderRequest;
import com.electronic.store.dtos.OrderDto;
import com.electronic.store.dtos.PageableResponse;

import java.util.List;

public interface OrderService{
    //create order

    //remove order
    void removeOrder(String orderId);

    //get order of user
    List<OrderDto> getOrdersOfUser(String userId);

    //get orders
        PageableResponse<OrderDto> getOrders(int pageNo, int pageSize, String sortBy, String sortDir);

    }
