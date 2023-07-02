package com.electronic.store.services.impl;

import com.electronic.store.dtos.UserDto;
import com.electronic.store.entities.User;
import com.electronic.store.repositories.UserRepository;
import com.electronic.store.services.UserService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ModelMapper mapper;

    @InjectMocks
    private UserServiceImpl userServiceimpl ;

    static User user;
    static User user1;

    static UserDto dto;
    static UserDto dto1;

    @BeforeAll
    public static void init(){

        user= User.builder().userId("1")
                .email("kirtiSalunke@gmail.com")
                .name("kirti")
                .gender("female")
                .password("kirti1")
                .imageName("kirti.png")
                .about("I am a software engineer")
                .build();


        user1 = User.builder().userId("1")
            .email("kirtiSalunke@gmail.com")
            .name("kirti")
            .gender("female")
            .password("kirti1")
            .imageName("kirti.png")
            .about("I am a software engineer")
            .build();

        dto= UserDto.builder().userId("1")
                .email("kirtiSalunke@gmail.com")
                .name("kirti")
                .gender("female")
                .password("kirti1")
                .imageName("kirti.png")
                .about("I am a software engineer")
                .build();

        dto1= UserDto.builder().userId("1")
                .email("kirtiSalunke@gmail.com")
                .name("kirti")
                .gender("female")
                .password("kirti1")
                .imageName("kirti.png")
                .about("I am a software engineer")
                .build();



    }

    @Test
    void createUser() {
            when(mapper.map(dto, User.class)).thenReturn(user);
            when(userRepository.save(user)).thenReturn(user);
            when(mapper.map(user, UserDto.class)).thenReturn(dto);

            String name = userServiceimpl.createUser(dto).getName();

            assertEquals("Ekta", name);
    }
}