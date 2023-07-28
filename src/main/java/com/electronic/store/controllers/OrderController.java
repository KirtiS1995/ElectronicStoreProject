package com.electronic.store.controllers;

import com.electronic.store.dtos.CreateOrderRequest;
import com.electronic.store.dtos.OrderDto;
import com.electronic.store.dtos.PageableResponse;
import com.electronic.store.helper.ApiResponse;
import com.electronic.store.helper.AppConstats;
import com.electronic.store.services.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private Logger logger = LoggerFactory.getLogger(OrderController.class);
    /**
     * @author Kirti
     * @apiNote  This api for creating order
     * @param request
     * @return
     */
    @PostMapping("/")
    public ResponseEntity<OrderDto> createOrder(@RequestBody CreateOrderRequest request){
        logger.info("Request initialized to create order ");
        OrderDto order = orderService.createOrder(request);
        logger.info(" Request completed to create order ");
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
        logger.info("Request initialized to remove order from cart :{}",orderId);
        orderService.removeOrder(orderId);
        ApiResponse response = ApiResponse.builder().message("Order removed .").status(HttpStatus.OK).success(true).build();
        logger.info("Request completed to remove order from cart :{}",orderId);
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
        logger.info("Request initialized for getting order of user :{}",userId);
        List<OrderDto> ordersOfUser = orderService.getOrdersOfUser(userId);
        logger.info("Request completed for getting order of user :{}",userId);
        return new ResponseEntity<>(ordersOfUser,HttpStatus.OK);
    }

    /**
     * @author Kirti
     * @apiNote  This api is for getOrders
     * @param pageNumber
     * @param pageSize
     * @param sortBy
     * @param sortDir
     * @return
     */
    @GetMapping("/users")
    public ResponseEntity<PageableResponse<OrderDto>> getOrders(
            @RequestParam(value = "pageNumber", defaultValue = AppConstats.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", defaultValue =AppConstats.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(value = "sortBy", defaultValue ="orderDate", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstats.SORT_DIR, required = false) String sortDir){
        logger.info("Request initialized for getting orders  :{}",pageNumber);
        PageableResponse<OrderDto> orders = orderService.getOrders(pageNumber, pageSize, sortBy, sortDir);
        logger.info("Request completed for getting orders  :{}",pageNumber);
        return new ResponseEntity<>(orders,HttpStatus.OK);
    }

    @PutMapping("/{orderId}")
    public ResponseEntity<OrderDto> getOrders(@PathVariable String orderId ,@RequestBody OrderDto orderDto){
        logger.info("Request initialized for updating  order info for orderId  :{}",orderId);
        OrderDto orderDto1 = orderService.updateOrder(orderId, orderDto);
        logger.info("Request completed for updating  order info for orderId  :{}",orderId);
        return new ResponseEntity<>(orderDto1,HttpStatus.OK);
    }
}
