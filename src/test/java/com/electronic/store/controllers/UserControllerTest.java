package com.electronic.store.controllers;

import com.electronic.store.dtos.ProductDto;
import com.electronic.store.dtos.UserDto;
import com.electronic.store.entities.User;
import com.electronic.store.services.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    private User user;

    @MockBean
    private UserService userService;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public  void init() {
        user = User.builder()
                .name("kirti")
                .email("kirti@gmail.com")
                .password("kirti123")
                .gender("female")
                .about("Testing method for create")
                .imageName("abc.png")
                .build();
    }

    @Test
    public void createUser() throws Exception {
        //users+Post + user data as json
        //data as jso+status created
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
    }

    @Test
    public void updateUserTest() throws Exception {
        //User/{usrrId}  +PUT request +json
        String userId ="123";
       UserDto userDto = UserDto.builder()
                .name("kirti salunke")
                .email("kirti@gmail.com")
                .password("kirti123")
                .gender("female")
                .about("Testing method for create")
                .imageName("xyz.png")
                .build();
       //        UserDto dto = mapper.map(user, UserDto.class);
        Mockito.when(userService.updateUser(Mockito.any(),Mockito.anyString())).thenReturn(userDto);
        this.mockMvc.perform(
                        MockMvcRequestBuilders.put("/users" +userId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(convertObjectToJsonString(user))
                                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").exists());
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