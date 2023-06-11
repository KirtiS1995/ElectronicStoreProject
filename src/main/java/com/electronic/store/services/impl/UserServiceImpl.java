package com.electronic.store.services.impl;

import com.electronic.store.dtos.PageableResponse;
import com.electronic.store.dtos.UserDto;
import com.electronic.store.entities.User;
import com.electronic.store.exceptions.ResourceNotFoundException;
import com.electronic.store.helper.AppConstats;
import com.electronic.store.helper.Helper;
import com.electronic.store.repositories.UserRepository;
import com.electronic.store.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Value("${user.profile.image.path}")
    private String imagePath;

    /**
     * * @author kirti
     * @implNote  This method is for Creating User
     * @param userDto
     * @return
     */
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
    /**
     *  @author kirti
     * @implNote  This method is for Updating User
     * @param userDto
     * @param userId
     * @return
     */
    @Override
    public UserDto updateUser(UserDto userDto, String userId) {
        log.info("Entering DAO call for updating User  with userId :{}",userId);
        User user = userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException(AppConstats.ID_NOT_FOUND+userId));
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
    /**
     * @author kirti
     * @implNote  This method is for delete User by Id
     * @return
     */
    @Override
    public void deleteUser(String userId) {
        log.info("Entering DAO call for deleting User  with userId :{}",userId);
        User user = userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException(AppConstats.ID_NOT_FOUND+userId));
        //   images/user/abc.png
        String fullPath = imagePath + user.getImageName();

        try{
            Path path = Paths.get(fullPath);
            Files.delete(path);
        }catch (NoSuchFileException ex){
            log.info("User Image not found in folder");
            ex.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }


        log.info("Completed DAO call for deleting User  with userId :{}",userId);
        userRepo.delete(user);
    }
    /**
      * @author kirti
     * @implNote  This Api is for Getting ALL Users
     * @return
     */
//    @Override
//    public List<UserDto> getALLUser() {
//        log.info("Entering DAO call for getting all User  ");
//        List<User> users = userRepo.findAll();
//        List<UserDto> dtoList = users.stream().map(user -> entityToDto(user)).collect(Collectors.toList());
//        log.info("Completed DAO call for getting all User  ");
//        return dtoList;
//    }

    @Override
    public PageableResponse<UserDto> getALLUser(int pageNumber, int pageSize, String sortBy, String sortDir) {
        log.info("Entering DAO call for getting all User with pageNumber And PageSize:{} ",pageNumber,pageSize);

        Sort sort=(sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());
        //default page no starts from zero..
        //pagenumber+1 for starting from 1
        Pageable pageable= PageRequest.of(pageNumber,pageSize,sort);

        Page<User> page = userRepo.findAll(pageable);
        PageableResponse<UserDto> response = Helper.getPageableResponse(page, UserDto.class);

        log.info("Completed DAO call for getting all Userwith pageNumber And PageSize:{} ",pageNumber,pageSize );
        return response;
    }

    /**
     * * @author kirti
     * @implNote  This method is for Getting Single User By ID
     * @return
     */
    @Override
    public UserDto getUserById(String userId) {
        log.info("Entering DAO call for getting User with userId:{} ",userId);
        User user = userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException(AppConstats.ID_NOT_FOUND));
        log.info("Completed DAO call for getting User with userId:{} ",userId);
        return entityToDto(user);
    }
    /**
     *  @author kirti
     * @implNote  This api is for Getting USer by email
     * @param email
     * @return
     */
    @Override
    public UserDto getUserByEmail(String email) {
        log.info("Entering DAO call for getting User with email:{} ",email);
        User user = userRepo.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException(AppConstats.EMAIL_NOT_FOUND +email));
        log.info("Completed DAO call for getting User with email:{} ",email);
        return entityToDto(user);
    }
    /**
     *  @author kirti
     * @implNote  This api is for searching User using keywords
     * @param keyword
     * @return
     */
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
