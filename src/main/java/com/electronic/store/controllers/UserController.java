package com.electronic.store.controllers;

import com.electronic.store.dtos.PageableResponse;
import com.electronic.store.dtos.UserDto;
import com.electronic.store.helper.ApiResponse;
import com.electronic.store.helper.AppConstats;
import com.electronic.store.helper.ImageResponse;
import com.electronic.store.services.FileService;
import com.electronic.store.services.UserService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import org.slf4j.Logger;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequestMapping("/users")
//@Slf4j
public class UserController {
    Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private FileService fileService;

    @Value("${user.profile.image.path}")
    private String imageUploadPath;

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
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable String userId)
    {
        logger.info("Request entering for deleting User with userID : {}",userId);
        userService.deleteUser(userId);
        ApiResponse message=ApiResponse.builder().message(AppConstats.USER_DELETED+userId)
                .success(true).status(HttpStatus.OK).build();
        logger.info("Completed Request for deleting  User with userID : {}",userId);
        return new ResponseEntity<ApiResponse>(message,HttpStatus.OK);
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
//

        @GetMapping
        public ResponseEntity<PageableResponse<UserDto>> getAllUsers(
                @RequestParam(value = "pageNumber",defaultValue = AppConstats.PAGE_NUMBER,required = false) int pageNumber,
                @RequestParam(value = "pageSize",defaultValue = AppConstats.PAGE_SIZE,required = false) int pageSize,
                @RequestParam(value = "sortBy",defaultValue = AppConstats.SORT_BY,required = false) String sortBy,
                @RequestParam(value = "sortDir",defaultValue =AppConstats.SORT_DIR,required = false) String sortDir

        )
        {
            logger.info("Request entering for Getting all Users ");
            PageableResponse<UserDto> allUser = userService.getALLUser(pageNumber, pageSize, sortBy, sortDir);
            logger.info("Completed Request for Getting all Users");
            return new ResponseEntity<PageableResponse<UserDto>>(allUser,HttpStatus.OK);
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
    /**
     * @apiNote This api is for uploading image for User
     * @param userId
     * @param  image
     * @return
     */
    // User image upload
    @PostMapping("/image/{userId}")
    public ResponseEntity<ImageResponse> uploadUserImage(@RequestPart("userImage") MultipartFile image,
                                                         @PathVariable String userId) throws IOException {
        logger.info("Request entering for uploading image  ");
              String imageName = this.fileService.uploadImage(imageUploadPath, image);
        UserDto user = this.userService.getUserById(userId);
        user.setImageName(imageName);
        UserDto userDto = userService.updateUser(user, userId);
        ImageResponse response=ImageResponse.builder().imageName(imageName).message(AppConstats.IMAGE_UPLOAD).success(true).status(HttpStatus.CREATED).build();
        logger.info("Request completed for uploading image  ");
        return new ResponseEntity<ImageResponse>(response, HttpStatus.CREATED);
    }
/**
     * @apiNote This api is for serving image
     * @param userId
     * @param  response
     * @return
     */
    //method to serve files
    @GetMapping("/image/{userId}")
    public void serveImage( @PathVariable String userId,
            HttpServletResponse response ) throws IOException {

        UserDto user = this.userService.getUserById(userId);
        logger.info("User Iamge name: {}",user.getImageName());
            InputStream resource = this.fileService.getResource(imageUploadPath, user.getImageName());
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource,response.getOutputStream());
    }

}
