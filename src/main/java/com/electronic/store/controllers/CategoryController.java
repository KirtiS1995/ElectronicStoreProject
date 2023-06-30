package com.electronic.store.controllers;

import com.electronic.store.dtos.CategoryDto;
import com.electronic.store.dtos.PageableResponse;
import com.electronic.store.dtos.ProductDto;
import com.electronic.store.dtos.UserDto;
import com.electronic.store.helper.ApiResponse;
import com.electronic.store.helper.AppConstats;
import com.electronic.store.helper.ImageResponse;
import com.electronic.store.services.CategoryService;
import com.electronic.store.services.FileService;
import com.electronic.store.services.ProductService;
import com.electronic.store.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/categories")

public class CategoryController {
    Logger logger = LoggerFactory.getLogger(CategoryController.class);

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductService productService;

    @Autowired
    private FileService fileService;

    @Value("${category.image.path}")
    private String imageUploadPath;

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
    public ResponseEntity<CategoryDto> updateCategory(@Valid @RequestBody CategoryDto categoryDto, @PathVariable String categoryId)
    {
        logger.info("Request entering for updating  Category with category ID : {}",categoryId);
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
    public ResponseEntity<CategoryDto> getSingleCategory(@PathVariable String categoryId)
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

    /**
     * @apiNote This api is for uploading image for category
     * @param categoryId
     * @param  image
     * @return
     */
    // User image upload
    @PostMapping("/image/{categoryId}")
    public ResponseEntity<ImageResponse> uploadCategoryImage(@RequestPart("categoryImage") MultipartFile image,
                                                         @PathVariable String categoryId) throws IOException {
        logger.info("Request entering for uploading image with categoryId :{} ",categoryId);
        String imageName = this.fileService.uploadImage(imageUploadPath, image);
        CategoryDto category = this.categoryService.getSingleCategory(categoryId);
        category.setCoverImage(imageName);
        CategoryDto categoryDto = categoryService.updateCategory(category, categoryId);
        ImageResponse response=ImageResponse.builder().imageName(imageName).message(AppConstats.IMAGE_UPLOAD).success(true).status(HttpStatus.CREATED).build();
        logger.info("Request completed for uploading image with categoryId :{}  ",categoryId);
        return new ResponseEntity<ImageResponse>(response, HttpStatus.CREATED);
    }
    /**
     * @apiNote This api is for serving image
     * @param categoryId
     * @param  response
     * @return
     */
    //method to serve image
    @GetMapping("/image/{categoryId}")
    public void serveImage( @PathVariable String categoryId,
                            HttpServletResponse response ) throws IOException {
        CategoryDto category = this.categoryService.getSingleCategory(categoryId);
        logger.info("Category Cover Image name: {}",category.getCoverImage());
        InputStream resource = this.fileService.getResource(imageUploadPath, category.getCoverImage());
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource,response.getOutputStream());
    }

    /**
     * @apiNote This api is for Creating product with category
     * @param productDto
     * @param  categoryId
     * @return
     */
    @PostMapping("/{categoryId}/products")
    public ResponseEntity<ProductDto> createProductWithCategory(@RequestBody ProductDto productDto,@PathVariable String categoryId)
    {
        logger.info("Request entering for create product with category:{}",categoryId);
        ProductDto productWithCategory = productService.createWithCategory(productDto, categoryId);
        logger.info("Completed Request for create product with category:{}",categoryId);
        return new ResponseEntity<ProductDto>(productWithCategory, HttpStatus.CREATED);
    }

    //Update category of product
    /**
     * @apiNote This api is for updating category for product
     * @param productId
     * @param  categoryId
     * @return
     */
    @PutMapping("/{categoryId}/products/{productId}")
    public ResponseEntity<ProductDto> updateCategoryOfProduct(@PathVariable String categoryId,
                                                              @PathVariable String productId)
    {
        logger.info("Request entering for updating product with category:{}",productId);
        ProductDto productDto = productService.updateCategory(productId, categoryId);
        logger.info("Complete Request for updating product with category:{}",productId);
        return new ResponseEntity<>(productDto,HttpStatus.OK);
    }

    //get product of categories
    /**
     * @apiNote This api is for getting  product of categories
     * * @param productId
     * @return
     */
    @GetMapping("/{categoryId}/products")
    public ResponseEntity<PageableResponse<ProductDto>> getProductOfCategory(@PathVariable String categoryId,
    @RequestParam(value = "pageNumber",defaultValue = AppConstats.PAGE_NUMBER,required = false) int pageNumber,
       @RequestParam(value = "pageSize",defaultValue = AppConstats.PAGE_SIZE,required = false) int pageSize,
       @RequestParam(value = "sortBy",defaultValue = AppConstats.SORT_BY_CAT,required = false) String sortBy,
       @RequestParam(value = "sortDir",defaultValue =AppConstats.SORT_DIR,required = false) String sortDir                                                                    )
    {
        logger.info("Request entering for  getting  product of categories with categoryId:{}",categoryId);
        PageableResponse<ProductDto> response = productService.getAllOfCategory(categoryId,pageNumber,pageSize,sortBy,sortDir);
        logger.info("Request completed for getting  product of categories with categoryId:{}",categoryId);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

}
