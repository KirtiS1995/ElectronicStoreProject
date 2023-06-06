package com.electronic.store.controllers;

import com.electronic.store.dtos.ApiResponseMessage;
import com.electronic.store.dtos.UserDto;
import com.electronic.store.entities.User;
import com.electronic.store.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.slf4j.Logger;

import javax.validation.Valid;

@RestController
@RequestMapping("/users")
//@Slf4j
public class UserController {
    Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    /**
     * @apiNote This api is for Creating User
     * @param userDto
     * @return
     */
    @PostMapping
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto)
    {		logger.info("Request entering for create User");
        UserDto user = userService.createUser(userDto);
        logger.info("Completed Request for create User");
        return new ResponseEntity<UserDto>(user,HttpStatus.CREATED);
    }

    /**
     * @apiNote This api is for Updating User
     * @param userDto
     * @param userId
     * @return
     */
    @PutMapping("/{userId}")
    public ResponseEntity<UserDto> updateUser(@Valid @RequestBody UserDto userDto, @PathVariable String userId)
    {
        logger.info("Request entering for updating  User with userID : {}",userId);
        UserDto userDto1 = userService.updateUser(userDto, userId);
        logger.info("Completed Request for updating  User with userID : {}",userId);
        return new ResponseEntity<UserDto>(userDto1,HttpStatus.OK);
    }


    /**
     * @apiNote This Api is for Get Single User By ID
     * @return
     */
    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getSingleUser(@PathVariable String userId)
    {
        logger.info("Request entering for getting User with userID : {}",userId);
        UserDto user = userService.getUserById(userId);
        logger.info("Completed Request for getting  User with userID : {}",userId);
        return new ResponseEntity<UserDto>(user,HttpStatus.OK);
    }

    /**
     * @apiNote This Api is for delete User by Id
     * @return
     */
    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponseMessage> deleteUser(@PathVariable String userId)
    {
        logger.info("Request entering for deleting User with userID : {}",userId);
        userService.deleteUser(userId);
        ApiResponseMessage message=ApiResponseMessage.builder().message("User Deleted Succesfully")
                .success(true).status(HttpStatus.OK).build();
        logger.info("Completed Request for deleting  User with userID : {}",userId);
        return new ResponseEntity<ApiResponseMessage>(message,HttpStatus.OK);
    }

    /**
     * @apiNote This api is for Getting USer by email
     * @param email
     * @return
     */
    @GetMapping("/email/{email}")
    public ResponseEntity<UserDto> getUserByEmail(@PathVariable String email)
    {
        logger.info("Request entering for getting User with email : {}",email);
        UserDto userByEmail = userService.getUserByEmail(email);
        logger.info("Completed Request for getting User with email : {}",email);
        return new ResponseEntity<UserDto>(userByEmail,HttpStatus.OK);
    }

        /**
         * @apiNote This Api is for Getting ALL Users
         * @return
         */
        @GetMapping
        public ResponseEntity<List<UserDto>> getAllUsers()
        {
            logger.info("Request entering for Getting all Users ");
            List<UserDto> allUser = userService.getALLUser();
            logger.info("Completed Request for Getting all Users");
            return new ResponseEntity<List<UserDto>>(allUser,HttpStatus.OK);
        }
    /**
     * @apiNote This api is for searching User using keywords
     * @param keyword
     * @return
     */
    @GetMapping("/search/{keyword}")
    public ResponseEntity<List<UserDto>> searchUser(@PathVariable String keyword)
    {
        logger.info("Request entering for searching User with name : {}",keyword);
        List<UserDto> userDtos = userService.searchUser(keyword);
        logger.info("Completed Request for searching  User with name : {}",keyword);
        return new ResponseEntity<List<UserDto>>(userDtos,HttpStatus.OK);
    }

}
