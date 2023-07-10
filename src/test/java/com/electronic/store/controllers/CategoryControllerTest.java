package com.electronic.store.controllers;

import com.electronic.store.dtos.CategoryDto;
import com.electronic.store.dtos.UserDto;
import com.electronic.store.entities.Category;
import com.electronic.store.entities.User;
import com.electronic.store.repositories.CategoryRepository;
import com.electronic.store.services.CategoryService;
import com.electronic.store.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CategoryControllerTest {

    private Category category;

     @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ModelMapper mapper;

    @MockBean
    private CategoryService categoryService;

    @BeforeEach
    public void init(){
        category =Category.builder()
                .title("TV")
                .description("Category realted to TV")
                .coverImage("xyz.png")
                .build();
    }
    @Test
    void createTest() throws Exception {
        CategoryDto dto = mapper.map(category, CategoryDto.class);
        Mockito.when(categoryService.create(Mockito.any())).thenReturn(dto);
        //actual request for url
        this.mockMvc.perform(
                        MockMvcRequestBuilders.post("/categories/")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(convertObjectToJsonString(category))
                                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").exists());
    }

    @Test
    void updateCategoryTest() {
    }

    @Test
    void getSingleCategoryTest() {
    }

    @Test
    void deleteCategoryTest() {
    }

    @Test
    void searchCategoryTest() {
    }

    @Test
    void getAllCategoriesTest() {
    }

    private String convertObjectToJsonString(Object user) {
        try {
            return new ObjectMapper().writeValueAsString(user);
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}