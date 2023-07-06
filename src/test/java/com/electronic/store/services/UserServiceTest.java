package com.electronic.store.services;

import com.electronic.store.dtos.UserDto;
import com.electronic.store.entities.User;
import com.electronic.store.repositories.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

@SpringBootTest
public class UserServiceTest {

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private ModelMapper mapper;

//    @InjectMocks
    @Autowired
    private UserService userService;

    User user;

    @BeforeEach
    public void init(){
        user = User.builder()
                .name("kirti")
                .email("kirti@gmail.com")
                .password("kirti")
                .gender("female")
                .about("Testing method for create")
                .imageName("abc.png")
                .build();
    }

    //create user
    @Test
    public void createUserTest()
    {
        Mockito.when(userRepository.save(Mockito.any())).thenReturn(user);
        UserDto user1 = userService.createUser(mapper.map(user,UserDto.class));
        System.out.println(user1.getName());
        Assertions.assertNotNull(user1);
        Assertions.assertEquals("kirti",user1.getName());
    }

    //Update user Test
    @Test
    public void updateUserTest(){
        String  userId="aabbccdd";
        UserDto userDto = UserDto.builder()
                .name("kirti salunke")
                .password("kirti")
                .gender("female")
                .about("This is updated user")
                .imageName("xyz.png")
                .build();

            Mockito.when(userRepository.findById(Mockito.anyString())).thenReturn(Optional.of(user));
            Mockito.when(userRepository.save(Mockito.any())).thenReturn(user);

        UserDto updatedUser = userService.updateUser(userDto, userId);
//        UserDto updatedUser =mapper.map(user,UserDto.class);            //Not updated old value get printed

        System.out.println(updatedUser.getName());
        System.out.println(updatedUser.getImageName());
        Assertions.assertNotNull(userDto);
        Assertions.assertEquals(userDto.getName(),updatedUser.getName(),"Name is not validated");

    }


}
