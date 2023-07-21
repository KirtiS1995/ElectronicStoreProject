package com.electronic.store.services.impl;

import com.electronic.store.dtos.AddItemToCartRequest;
import com.electronic.store.dtos.CartDto;
import com.electronic.store.entities.Cart;
import com.electronic.store.entities.CartItem;
import com.electronic.store.entities.Product;
import com.electronic.store.entities.User;
import com.electronic.store.exceptions.BadApiRequestException;
import com.electronic.store.exceptions.ResourceNotFoundException;
import com.electronic.store.helper.AppConstats;
import com.electronic.store.repositories.CartItemRepository;
import com.electronic.store.repositories.CartRepository;
import com.electronic.store.repositories.ProductRepository;
import com.electronic.store.repositories.UserRepository;
import com.electronic.store.services.CartService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
@Service
public class CartServiceImpl implements CartService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepo;
    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private CartItemRepository cartItemRepository;

    private static  final Logger logger= LoggerFactory.getLogger(CartServiceImpl.class);

    /**
     * @author Kirti
     * @implNote This method is for adding item to cart
     * @param userId
     * @param request
     * @return
     */
    @Override
    public CartDto addItemToCart(String userId, AddItemToCartRequest request) {
        logger.info("Dao Request initialized for adding item to cart ");
        String productId = request.getProductId();
        Integer quantity = request.getQuantity();

            if (quantity<=0)
        {
            throw new BadApiRequestException(AppConstats.QUANTITY_NOT_VALID);
        }
        //Fetch The product
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException(AppConstats.PRODUCT_NOT_FOUND+productId));

            //fetch the User from Database
        User user = userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException(AppConstats.ID_NOT_FOUND+userId));

        Cart cart=null;
        try {
            logger.info("Find Cart BY User");
            cart = cartRepository.findByUser(user).get();
        }catch (NoSuchElementException e)
        {
            logger.info("If Cart Not Available then Create New Cart");
            cart=new  Cart();
            cart.setCartId(UUID.randomUUID().toString());
            cart.setCreatedAt(new Date());

        }
        //Perform Cart Operation
        //If Cart item Already Present Then update Cart
        AtomicBoolean updated= new AtomicBoolean(false);
        Set<CartItem> cartItem = cart.getCartItem();
        logger.info("If Cart Already exists then update cart");
        Set<CartItem> updatedList = cartItem.stream().map(item -> {
                    if (item.getProduct().getProductId().equals(productId)) {
                        //product Already present
                        item.setQuantity(quantity);
                        item.setTotalPrices(quantity * product.getDiscountedPrice());
                        updated.set(true);
                    }
                    return item;
                }
        ).collect(Collectors.toSet());

        //cart.setCartItem(updatedList);
        cart.getCartItem().addAll(updatedList);
        if(!updated.get()) {
            CartItem item = CartItem.builder().quantity(quantity).product(product).cart(cart)
                    .totalPrices(quantity * product.getDiscountedPrice()).build();
            cart.getCartItem().add(item);

        }
        cart.setUser(user);
        cart.setLastModifiedBy(user.getName());
        cart.setCreatedBy(user.getName());
        cart.setIsActive("True");
        Cart cart1= cartRepository.save(cart);
        logger.info("Dao Request completed for adding item to cart ");
        return mapper.map(cart1,CartDto.class);    }

    /**
     * @author Kirti
     * @implNote  Method for removing item from  cart
     * @param userId
     * @param cartItemId
     */
    @Override
    public void removeItemFromCart(String userId, int cartItemId) {
        logger.info("Dao Request initialized for removing item from cart :{}",cartItemId);
        CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(() -> new ResourceNotFoundException(AppConstats.CART_NOT_FOUND+cartItemId));
        cartItemRepository.delete(cartItem);
        logger.info("Dao Request completed for removing item from cart :{}",cartItemId);
    }

    /**
     * @implNote Method for clearing cart
     * @author Kirti
     * @param userId
     */
    @Override
    public void clearCart(String userId) {
        logger.info("Dao Request initialized to clear cart :{}",userId);
        User user = userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException(AppConstats.ID_NOT_FOUND+userId));
        Cart cart = cartRepository.findByUser(user).orElseThrow(() -> new ResourceNotFoundException(AppConstats.CART_NOT_FOUND));
        cart.getCartItem().clear();
        cartRepository.save(cart);
        logger.info("Dao Request completed to clear cart :{}",userId);
    }

    /**
     * @author Kirti
     * @implNote Method for getting cart by user
     * @param userId
     * @return
     */
    @Override
    public CartDto getCartByUSer(String userId) {
        logger.info("Dao Request initialized to get cart by user :{}",userId);
        User user = userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException(AppConstats.ID_NOT_FOUND+userId));
        Cart cart = cartRepository.findByUser(user).orElseThrow(() -> new ResourceNotFoundException(AppConstats.CART_NOT_FOUND));
        logger.info("Dao Request completed to get cart by user :{}",userId);
        return mapper.map(cart,CartDto.class);
    }
}
