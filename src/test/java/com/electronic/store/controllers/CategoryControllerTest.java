package com.electronic.store.controllers;

<<<<<<< HEAD
import com.electronic.store.dtos.UserDto;
=======
>>>>>>> 66c088a (Searching user Test for controller.)
import com.electronic.store.entities.Category;
import com.electronic.store.entities.User;
import com.electronic.store.repositories.CategoryRepository;
import com.electronic.store.services.CategoryService;
import com.electronic.store.services.UserService;
<<<<<<< HEAD
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
=======
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
>>>>>>> 66c088a (Searching user Test for controller.)
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
<<<<<<< HEAD
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

=======
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
>>>>>>> 66c088a (Searching user Test for controller.)
@SpringBootTest
@AutoConfigureMockMvc
class CategoryControllerTest {

<<<<<<< HEAD
    private Category category;

     @Autowired
    private MockMvc mockMvc;
=======

    private User user;

    @MockBean
    private UserService userService;
>>>>>>> 66c088a (Searching user Test for controller.)

    @Autowired
    private ModelMapper mapper;

<<<<<<< HEAD
    @MockBean
    private CategoryService categoryService;

=======
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ModelMapper mapper;

    @Autowired
    private CategoryService categoryService;

    Category category;

>>>>>>> 66c088a (Searching user Test for controller.)
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
<<<<<<< HEAD
        UserDto dto = mapper.map(user, UserDto.class);
        Mockito.when(userService.createUser(Mockito.any())).thenReturn(dto);
        //actual request for url
        this.mockMvc.perform(
                        MockMvcRequestBuilders.post("/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(convertObjectToJsonString(user))
                                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").exists());
=======

>>>>>>> 66c088a (Searching user Test for controller.)
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
<<<<<<< HEAD

    private String convertObjectToJsonString(Object user) {
        try {
            return new ObjectMapper().writeValueAsString(user);
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
=======
>>>>>>> 66c088a (Searching user Test for controller.)
}