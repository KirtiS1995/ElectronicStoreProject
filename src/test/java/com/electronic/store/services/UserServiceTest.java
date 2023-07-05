package com.electronic.store.services;

import com.electronic.store.entities.User;
import com.electronic.store.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
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
        Mockito.when(userRepository.save(Mockito.any())).thenReturn( )
    }
}
