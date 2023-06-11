package com.electronic.store.controllers;

import com.electronic.store.dtos.CategoryDto;
import com.electronic.store.dtos.UserDto;
import com.electronic.store.helper.ApiResponse;
import com.electronic.store.helper.AppConstats;
import com.electronic.store.services.CategoryService;
import com.electronic.store.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/categories")

public class CategoryController {
    Logger logger = LoggerFactory.getLogger(CategoryController.class);

    @Autowired
    private CategoryService categoryService;


    /**
     * @apiNote This api is for Creating Catgeory
     * @param categoryDto
     * @return
     */
    @PostMapping("/")
    public ResponseEntity<CategoryDto> create(@Valid @RequestBody CategoryDto categoryDto)
    {		logger.info("Request entering for create category");
        CategoryDto categoryDto1 = categoryService.create(categoryDto);
        logger.info("Completed Request for create category");
        return new ResponseEntity<CategoryDto>(categoryDto1, HttpStatus.CREATED);
    }

    /**
     * @apiNote This api is for Updating User
     * @param categoryDto
     * @param categoryId
     * @return
     */
    @PutMapping("/{categoryId}")
    public ResponseEntity<CategoryDto> updateUser(@Valid @RequestBody CategoryDto categoryDto, @PathVariable String categoryId)
    {
        logger.info("Request entering for updating  Categopry with category ID : {}",categoryId);
        CategoryDto categoryDto1 = categoryService.updateCategory(categoryDto, categoryId);
        logger.info("Completed Request for updating  Category with categoryID : {}",categoryId);
        return new ResponseEntity<CategoryDto>(categoryDto1,HttpStatus.OK);
    }

    /**
     * @apiNote This Api is for Get Single User By ID
     * @param catgeoryId
     * @return
     */
    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryDto> getSingleCategory(@PathVariable String catgeoryId)
    {
        logger.info("Request entering for getting category with CategoryId : {}",catgeoryId);
        CategoryDto singleCategory = categoryService.getSingleCategory(catgeoryId);
        logger.info("Completed Request for getting  category with CategoryId : {}",catgeoryId);
        return new ResponseEntity<CategoryDto>(singleCategory,HttpStatus.OK);
    }

    /**
     * @apiNote This Api is for delete User by Id
     * @param categoryId
     * @return
     */
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<ApiResponse> deleteCategory(@PathVariable String categoryId)
    {
        logger.info("Request entering for deleting category with categoryId : {}",categoryId);
            categoryService.deleteCategory(categoryId);
        ApiResponse message=ApiResponse.builder().message(AppConstats.CATEGORY_DELETED+categoryId)
                .success(true).status(HttpStatus.OK).build();
        logger.info("Completed Request for deleting  category with categoryId : {}",categoryId);
        return new ResponseEntity<ApiResponse>(message,HttpStatus.OK);
    }
    /**
     * @apiNote This api is for searching User using keywords
     * @param keyword
     * @return
     */
    @GetMapping("/search/{keyword}")
    public ResponseEntity<List<CategoryDto>> searchCategory(@PathVariable String keyword)
    {
        logger.info("Request entering for searching Category with name : {}",keyword);
        List<UserDto> userDtos = userService.searchUser(keyword);
        logger.info("Completed Request for searching  Category with name : {}",keyword);
        return new ResponseEntity<List<CategoryDto>>(userDtos,HttpStatus.OK);
    }

}
