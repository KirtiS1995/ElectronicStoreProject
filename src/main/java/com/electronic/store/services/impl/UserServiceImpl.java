package com.electronic.store.services.impl;

import com.electronic.store.dtos.UserDto;
import com.electronic.store.entities.User;
import com.electronic.store.repositories.UserRepository;
import com.electronic.store.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public UserDto createUser(UserDto userDto) {
        log.info("Entering DAO call for creating User ");
        //Unique ID
        String userId = UUID.randomUUID().toString();
        userDto.setUserId(userId);
        //Convert Dto to entity
        User user = dtoToEntity(userDto);
        User savedUser = userRepo.save(user);
        //Convert Entity to Dto
        UserDto userDto1 = entityToDto(savedUser);
        log.info("Completed DAO call for creating User ");
        return userDto1;
    }

    @Override
    public UserDto updateUser(UserDto userDto, String userId) {
        log.info("Entering DAO call for updating User  with userId :{}",userId);
        User user = userRepo.findById(userId).orElseThrow(() -> new RuntimeException("User Not found with given ID"));
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
        user.setGender(userDto.getGender());
        user.setAbout(userDto.getAbout());
        user.setImageName(userDto.getImageName());

        User updatedUser = userRepo.save(user);
        UserDto updatedDto = entityToDto(updatedUser);
        log.info("Completed DAO call for updating User  with userId :{}",userId);
        return updatedDto;
    }

    @Override
    public void deleteUser(String userId) {
        log.info("Entering DAO call for deleting User  with userId :{}",userId);
        User user = userRepo.findById(userId).orElseThrow(() -> new RuntimeException("User not found with given id"));
        log.info("Completed DAO call for deleting User  with userId :{}",userId);
        userRepo.delete(user);
    }

    @Override
    public List<UserDto> getALLUser() {
        log.info("Entering DAO call for getting all User  ");
        List<User> users = userRepo.findAll();
        List<UserDto> dtoList = users.stream().map(user -> entityToDto(user)).collect(Collectors.toList());
        log.info("Completed DAO call for getting all User  ");
        return dtoList;
    }

    @Override
    public UserDto getUserById(String userId) {
        log.info("Entering DAO call for getting User with userId:{} ",userId);
        User user = userRepo.findById(userId).orElseThrow(() -> new RuntimeException("User not found with given id"));
        log.info("Completed DAO call for getting User with userId:{} ",userId);
        return entityToDto(user);
    }

    @Override
    public UserDto getUserByEmail(String email) {
        log.info("Entering DAO call for getting User with email:{} ",email);
        User user = userRepo.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found with given email"));
        log.info("Completed DAO call for getting User with email:{} ",email);
        return entityToDto(user);
    }

    @Override
    public List<UserDto> searchUser(String keyword) {
        log.info("Entering DAO call for searching User with keyword:{} ",keyword);
        List<User> users = userRepo.findByNameContaining(keyword);
        List<UserDto> dtoList = users.stream().map(user -> entityToDto(user)).collect(Collectors.toList());
        log.info("Completed DAO call for searching User with keyword:{} ",keyword);
        return dtoList;
    }
    private UserDto entityToDto(User savedUser)
    {
//        UserDto userDto = UserDto.builder()
//                .userId(savedUser.getUserId())
//                .name(savedUser.getName())
//                .email(savedUser.getEmail())
//                .password(savedUser.getPassword())
//                .about(savedUser.getAbout())
//                .gender(savedUser.getGender())
//                .imageName(savedUser.getImageName())
//                .build();
        return modelMapper.map(savedUser,UserDto.class);
    }
    private User dtoToEntity(UserDto userDto)
    {
//        User user = User.builder()
//                .userId(userDto.getUserId())
//                .name(userDto.getName())
//                .email(userDto.getEmail())
//                .password(userDto.getPassword())
//                .about(userDto.getAbout())
//                .gender(userDto.getGender())
//                .imageName(userDto.getImageName())
//                .build();
        return modelMapper.map(userDto,User.class);
    }
}
