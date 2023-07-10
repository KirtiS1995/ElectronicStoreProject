package com.electronic.store.controllers;

import com.electronic.store.dtos.PageableResponse;
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

import java.util.Arrays;

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
                        MockMvcRequestBuilders.put("/users/" +userId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(convertObjectToJsonString(userDto))
                                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").exists());
   }

    @Test
    void getSingleUserTest() throws Exception {

        String userId ="123";
        UserDto userDto = this.mapper.map(user, UserDto.class);
        Mockito.when(userService.getUserById(Mockito.anyString())).thenReturn(userDto);
        this.mockMvc.perform(
                        MockMvcRequestBuilders.get("/users/"+userId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").exists());


    }
    @Test
     void getAllUserTest() throws Exception {

        UserDto userDto1 = UserDto.builder()
                .name("shlok ")
                .email("shlok@gmail.com")
                .password("shlok123")
                .gender("female")
                .about("Testing method for getting all user")
                .imageName("xyz.png")
                .build();
        UserDto userDto2 = UserDto.builder()
                .name("siya")
                .email("siya@gmail.com")
                .password("siya123")
                .gender("female")
                .about("Testing method for getting all user")
                .imageName("xyz.png")
                .build();
        UserDto userDto3 = UserDto.builder()
                .name("jiya salunke")
                .email("jiya@gmail.com")
                .password("jiya123")
                .gender("female")
                .about("Testing method for getting all user")
                .imageName("xyz.png")
                .build();

        PageableResponse<UserDto> pageableResponse= new PageableResponse<>();

           pageableResponse.setLastPage(false);
        pageableResponse.setTotalElements(2000);
        pageableResponse.setPageNumber(50);
        pageableResponse.setContent(Arrays.asList(userDto1,userDto2,userDto3));
        pageableResponse.setTotalPages(200);
        pageableResponse.setPageSize(20);

        Mockito.when(userService.getALLUser(Mockito.anyInt(),Mockito.anyInt(),Mockito.anyString(),Mockito.anyString())).thenReturn(pageableResponse);

        //request for url
        this.mockMvc.perform(
                        MockMvcRequestBuilders.get("/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
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