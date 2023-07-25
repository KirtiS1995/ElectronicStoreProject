package com.electronic.store.controllers;

import com.electronic.store.dtos.CreateOrderRequest;
import com.electronic.store.dtos.OrderDto;
import com.electronic.store.dtos.PageableResponse;
import com.electronic.store.helper.ApiResponse;
import com.electronic.store.helper.AppConstats;
import com.electronic.store.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * @author Kirti
     * @apiNote  This api for creating order
     * @param request
     * @return
     */
    @PostMapping("/")
    public ResponseEntity<OrderDto> createOrder(@RequestBody CreateOrderRequest request){
        OrderDto order = orderService.createOrder(request);
        return new ResponseEntity<>(order, HttpStatus.CREATED);
    }

    /**
     * @author Kirti
     * @apiNote  This api for removing order of user
     * @param orderId
     * @return
     */
    @DeleteMapping("/{orderId}")
    public ResponseEntity<ApiResponse> removeOrder(@PathVariable String orderId){
        orderService.removeOrder(orderId);
        ApiResponse response = ApiResponse.builder().message("Order removed .").status(HttpStatus.OK).success(true).build();
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    /**
     * @author kirti
     * @apiNote This api is for getting order of user
     * @param userId
     * @return
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OrderDto>> getOrdersOfUser(@PathVariable String userId){
        List<OrderDto> ordersOfUser = orderService.getOrdersOfUser(userId);
        return new ResponseEntity<>(ordersOfUser,HttpStatus.OK);
    }

    @GetMapping("/users")
    public ResponseEntity<PageableResponse<OrderDto>> getOrders(
            @RequestParam(value = "pageNumber", defaultValue = AppConstats.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", defaultValue =AppConstats.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(value = "sortBy", defaultValue ="orderDate", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstats.SORT_DIR, required = false) String sortDir){

        PageableResponse<OrderDto> orders = orderService.getOrders(pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(orders,HttpStatus.OK);
    }

}
