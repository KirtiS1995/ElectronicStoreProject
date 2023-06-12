package com.electronic.store.controllers;

import com.electronic.store.dtos.CategoryDto;
import com.electronic.store.dtos.PageableResponse;
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
     * @apiNote This api is for Updating Category
     * @param categoryDto
     * @param categoryId
     * @return
     */
    @PutMapping("/{categoryId}")
    public ResponseEntity<CategoryDto> updateCategory(@Valid @RequestBody CategoryDto categoryDto, @PathVariable Integer categoryId)
    {
        logger.info("Request entering for updating  Categopry with category ID : {}",categoryId);
        CategoryDto categoryDto1 = categoryService.updateCategory(categoryDto, categoryId);
        logger.info("Completed Request for updating  Category with categoryID : {}",categoryId);
        return new ResponseEntity<CategoryDto>(categoryDto1,HttpStatus.OK);
    }

    /**
     * @apiNote This Api is for Get Single category By CategoryID
     * @param categoryId
     * @return
     */
    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryDto> getSingleCategory(@PathVariable Integer categoryId)
    {
        logger.info("Request entering for getting category with CategoryId : {}",categoryId);
        CategoryDto singleCategory = categoryService.getSingleCategory(categoryId);
        logger.info("Completed Request for getting  category with CategoryId : {}",categoryId);
        return new ResponseEntity<CategoryDto>(singleCategory,HttpStatus.OK);
    }

    /**
     * @apiNote This Api is for delete category by categoryId
     * @param categoryId
     * @return
     */
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<ApiResponse> deleteCategory(@PathVariable Integer categoryId)
    {
        logger.info("Request entering for deleting category with categoryId : {}",categoryId);
            categoryService.deleteCategory(categoryId);
        ApiResponse message=ApiResponse.builder().message(AppConstats.CATEGORY_DELETED+categoryId)
                .success(true).status(HttpStatus.OK).build();
        logger.info("Completed Request for deleting  category with categoryId : {}",categoryId);
        return new ResponseEntity<ApiResponse>(message,HttpStatus.OK);
    }
    /**
     * @apiNote This api is for searching category using keywords
     * @param keyword
     * @return
     */
    @GetMapping("/search/{keyword}")
    public ResponseEntity<List<CategoryDto>> searchCategory(@PathVariable String keyword)
    {
        logger.info("Request entering for searching Category with name : {}",keyword);
        List<CategoryDto> categoryDtos = categoryService.searchCategory(keyword);
        logger.info("Completed Request for searching  Category with name : {}",keyword);
        return new ResponseEntity<List<CategoryDto>>(categoryDtos,HttpStatus.OK);
    }
    /**
     * @apiNote This Api is for Getting ALL Categoris
     * @return
     */
 @GetMapping
    public ResponseEntity<PageableResponse<CategoryDto>> getAllCategories(
            @RequestParam(value = "pageNumber",defaultValue = AppConstats.PAGE_NUMBER,required = false) int pageNumber,
            @RequestParam(value = "pageSize",defaultValue = AppConstats.PAGE_SIZE,required = false) int pageSize,
            @RequestParam(value = "sortBy",defaultValue = AppConstats.SORT_BY_CAT,required = false) String sortBy,
            @RequestParam(value = "sortDir",defaultValue =AppConstats.SORT_DIR,required = false) String sortDir

    )
    {
        logger.info("Request entering for Getting all Categories ");
        PageableResponse<CategoryDto> allCategory = categoryService.getAllCategory(pageNumber, pageSize, sortBy, sortDir);
        logger.info("Completed Request for Getting all categories");
        return new ResponseEntity<PageableResponse<CategoryDto>>(allCategory,HttpStatus.OK);
    }

}
