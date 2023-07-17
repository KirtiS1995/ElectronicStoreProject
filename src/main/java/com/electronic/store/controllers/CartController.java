package com.electronic.store.controllers;

import com.electronic.store.dtos.AddItemToCartRequest;
import com.electronic.store.dtos.CartDto;
import com.electronic.store.helper.ApiResponse;
import com.electronic.store.helper.AppConstats;
import com.electronic.store.services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    /**
     * @apiNote  This api is for adding item to cart
     * @author Kirti
     * @param userId
     * @param request
     * @return
     */
    //Add item to cart
    @PostMapping("/{userId}")
    public ResponseEntity<CartDto> addItemToCart(@PathVariable String userId, @RequestBody AddItemToCartRequest request)
    {
        CartDto cartDto = cartService.addItemToCart(userId, request);
        return new   ResponseEntity<>(cartDto, HttpStatus.OK);
    }

    /**
     *  @author Kirti
     * @apiNote This api is for removing item from cart
     * @param userId
     * @param itemId
     * @return
     */
    @DeleteMapping("/{userId}/items/{itemId}")
    public ResponseEntity<ApiResponse> removeItemFromCart(@PathVariable String userId, @PathVariable int itemId)
    {
        cartService.removeItemFromCart(userId,itemId);
        ApiResponse response = ApiResponse.builder().message(AppConstats.CART_REMOVED)
                .success(true)
                .status(HttpStatus.OK)
                .build();
            return new ResponseEntity<ApiResponse>(response,HttpStatus.OK);
    }

    /**
     * @author Kirti
     * @apiNote This method is to clear cart.
     * @param userId
     * @return
     */
    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponse> clearCart(@PathVariable String userId)
    {
        cartService.clearCart(userId);
        ApiResponse response = ApiResponse.builder().message(AppConstats.CART_BLANK)
                .success(true)
                .status(HttpStatus.OK)
                .build();
        return new ResponseEntity<ApiResponse>(response,HttpStatus.OK);
    }

    /**
     * @author Kirti
     * @apiNote This method is for getting cart by user
     * @param userId
     * @return
     */
        @GetMapping("/{userId}")
        public ResponseEntity<CartDto> getCart(@PathVariable String userId)
        {
            CartDto cartDto = cartService.getCartByUSer(userId);
            return new ResponseEntity<>(cartDto, HttpStatus.OK);
        }

}
