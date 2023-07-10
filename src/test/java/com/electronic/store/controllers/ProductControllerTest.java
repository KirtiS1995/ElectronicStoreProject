package com.electronic.store.controllers;

import com.electronic.store.dtos.CategoryDto;
import com.electronic.store.dtos.ProductDto;
import com.electronic.store.entities.Category;
import com.electronic.store.entities.Product;
import com.electronic.store.services.CategoryService;
import com.electronic.store.services.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ProductControllerTest {
    private Product product;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ModelMapper mapper;

    @MockBean
    private ProductService productService;

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
    void createProductTest() throws Exception {
//        ProductDto dto = mapper.map(product, ProductDto.class);
        ProductDto dto = this.mapper.map(product, ProductDto.class);
        Mockito.when(productService.createProduct(Mockito.any())).thenReturn(dto);
        //actual request for url
        this.mockMvc.perform(
                        MockMvcRequestBuilders.post("/products/")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(convertObjectToJsonString(product))
                                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").exists());
    }



    @Test
    void updateProductTest() {
    }

    @Test
    void deleteProductTest() {
    }

    @Test
    void getSingleProductTest() {
    }

    @Test
    void getAllProductTest() {
    }

    @Test
    void getAllLiveProductTest() {
    }

    @Test
    void searchProductTest() {
    }

    private String convertObjectToJsonString(Object product) {
        try {
            return new ObjectMapper().writeValueAsString(product);
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}