package com.electronic.store.services;

import com.electronic.store.dtos.ProductDto;
import com.electronic.store.dtos.UserDto;
import com.electronic.store.entities.Product;
import com.electronic.store.entities.User;
import com.electronic.store.repositories.ProductRepository;
import com.electronic.store.repositories.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ProductServiceTest {
    @MockBean
    private ProductRepository productRepository;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private ProductService productService;

  Product product;

    @BeforeEach
    public void init(){
        product = Product.builder()
                .title("Samsung")
                .description("Phone having good camera")
                .price(120000)
                .discountedPrice(10000)
                .quantity(40)
                .live(true)
                .stock(false)
                .productImage("abc.png")
                .build();
    }

    @Test
   public void createProduct() {
        Mockito.when(productRepository.save(Mockito.any())).thenReturn(product);
        ProductDto product1 = productService.createProduct(mapper.map(product, ProductDto.class));
        System.out.println(product1.getTitle());
        Assertions.assertNotNull(product1);
        Assertions.assertEquals("Samsung",product1.getTitle());
    }

    @Test
    public void updateProduct() {
        String  productId="productId";
        ProductDto productDto = ProductDto.builder()
                .title("Iphone")
                .description("Phone having good camera")
                .price(120000)
                .discountedPrice(10000)
                .quantity(40)
                .live(true)
                .stock(false)
                .productImage("xyz.png")
                .build();

        Mockito.when(productRepository.findById(Mockito.anyString())).thenReturn(Optional.of(product));
        Mockito.when(productRepository.save(Mockito.any())).thenReturn(product);

        ProductDto productDto1 = productService.updateProduct(productDto, productId);

        System.out.println(productDto1.getTitle());
        System.out.println(productDto1.getProductImage());
        Assertions.assertNotNull(productDto1);
        Assertions.assertEquals(productDto.getTitle(),productDto1.getTitle(),"Title is not validated");
    }

    @Test
    public void deleteProduct() {
        String productId="id1";
        Mockito.when(productRepository.findById("id1")).thenReturn(Optional.of(product));
        productService.deleteProduct(productId);

        Mockito.verify(productRepository,Mockito.times(1)).delete(product);
    }

    @Test
    public void getSingleProduct() {
    }

    @Test
    public void getAll() {
    }

    @Test
    public void getAllLive() {
    }

    @Test
    public void searchByTitle() {
    }
}