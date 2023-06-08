package com.electronic.store.services;

import com.electronic.store.dtos.PageableResponse;
import com.electronic.store.dtos.UserDto;
import com.electronic.store.entities.User;

import java.util.List;

public interface UserService {

    //create
    UserDto createUser(UserDto userDto);

    //update
    UserDto updateUser(UserDto userDto,String userId);

    //delete
    void deleteUser(String userId);

    //getall users
    PageableResponse<UserDto> getALLUser(int pageNumber, int pageSize, String sortBy, String sortDir);

    //get single user by id
    UserDto getUserById(String userId);

    //get single user by email
    UserDto getUserByEmail(String email);


    //search user
    List<UserDto> searchUser(String keyword);

    //other user specific features


}
