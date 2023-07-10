package com.electronic.store.controllers;

import com.electronic.store.entities.Category;
import com.electronic.store.entities.User;
import com.electronic.store.repositories.CategoryRepository;
import com.electronic.store.services.CategoryService;
import com.electronic.store.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@AutoConfigureMockMvc
class CategoryControllerTest {


    private User user;

    @MockBean
    private UserService userService;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ModelMapper mapper;

    @Autowired
    private CategoryService categoryService;

    Category category;

    @BeforeEach
    public void init(){
        category =Category.builder()
                .title("TV")
                .description("Category realted to TV")
                .coverImage("xyz.png")
                .build();
    }
    @Test
    void createTest() {

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
}