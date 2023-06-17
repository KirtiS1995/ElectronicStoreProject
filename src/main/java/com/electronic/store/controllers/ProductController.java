package com.electronic.store.controllers;

import com.electronic.store.dtos.CategoryDto;
import com.electronic.store.dtos.PageableResponse;
import com.electronic.store.dtos.ProductDto;
import com.electronic.store.helper.ApiResponse;
import com.electronic.store.helper.AppConstats;
import com.electronic.store.services.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {
    Logger logger= LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private ProductService productService;

    //create
    /**
     * @apiNote This api is for Creating Product
     * @param productDto
     * @return
     */
    @PostMapping("/")
        public ResponseEntity<ProductDto> createProduct(@RequestBody ProductDto productDto)  {
                logger.info("Request entering for create category");
                ProductDto product = this.productService.createProduct(productDto);
                logger.info("Completed Request for create category");
                return new ResponseEntity<>(product, HttpStatus.CREATED);
        }

    //update
    /**
     * @apiNote This api is for Updating product details
     * @param productDto
     * @param productId
     * @return
     */
    @PutMapping("/{productId}")
    public ResponseEntity<ProductDto> updateProduct(@RequestBody ProductDto productDto, @PathVariable String productId)  {
        logger.info("Request entering for updating Product with productId:{}",productId);
        ProductDto product = this.productService.updateProduct(productDto, productId);
        logger.info("Request completed for updating Product with productId:{}",productId);
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    //delete
    /**
     * @apiNote This Api is for delete product by productId
     * @param productId
     * @return
     */
    @DeleteMapping("/{productId}")
    public ResponseEntity<ApiResponse> deleteProduct(@PathVariable String productId)  {
        logger.info("Request entering for deleting Product with productId:{}",productId);
        this.productService.deleteProduct(productId);
        ApiResponse message=ApiResponse.builder().message(AppConstats.PRODUCT_DELETED+productId)
                .success(true).status(HttpStatus.OK).build();
        logger.info("Request completed for deleting Product with productId:{}",productId);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    //get single
    /**
     * @apiNote This Api is for Get Single Product By productID
     * @param productId
     * @return
     */
    @GetMapping("/{productId}")
    public ResponseEntity<ProductDto> getSingleProduct( @PathVariable String productId)  {
        logger.info("Request entering for getting single Product with productId:{}",productId);
        ProductDto singleProduct = this.productService.getSingleProduct(productId);
        logger.info("Request completed for getting single Product with productId:{}",productId);
        return new ResponseEntity<>(singleProduct, HttpStatus.OK);
    }

    //get all
    /**
     * @apiNote This Api is for Getting ALL Product
     * @return
     */
    @GetMapping
    public ResponseEntity<PageableResponse<ProductDto>> getAllProduct(
            @RequestParam(value = "pageNumber",defaultValue = AppConstats.PAGE_NUMBER,required = false) int pageNumber,
            @RequestParam(value = "pageSize",defaultValue = AppConstats.PAGE_SIZE,required = false) int pageSize,
            @RequestParam(value = "sortBy",defaultValue = AppConstats.SORT_BY_CAT,required = false) String sortBy,
            @RequestParam(value = "sortDir",defaultValue =AppConstats.SORT_DIR,required = false) String sortDir)  {

        logger.info("Request entering for getting all Product");
        PageableResponse<ProductDto> allProduct = productService.getAll(pageNumber, pageSize, sortBy, sortDir);
        logger.info("Request completed for getting all Product");
        return new ResponseEntity<>(allProduct, HttpStatus.OK);
    }


    //get all Live
    /**
     * @apiNote This Api is for Getting ALL Live Product
     * @return
     */
    @GetMapping("/live")
    public ResponseEntity<PageableResponse<ProductDto>> getAllLiveProduct(
            @RequestParam(value = "pageNumber",defaultValue = AppConstats.PAGE_NUMBER,required = false) int pageNumber,
            @RequestParam(value = "pageSize",defaultValue = AppConstats.PAGE_SIZE,required = false) int pageSize,
            @RequestParam(value = "sortBy",defaultValue = AppConstats.SORT_BY_CAT,required = false) String sortBy,
            @RequestParam(value = "sortDir",defaultValue =AppConstats.SORT_DIR,required = false) String sortDir)  {

        logger.info("Request entering for getting all Live Product");
        PageableResponse<ProductDto> allProduct = productService.getAllLive(pageNumber, pageSize, sortBy, sortDir);
        logger.info("Request completed for getting all Live Product");
        return new ResponseEntity<>(allProduct, HttpStatus.OK);
    }


    //search
    /**
     * @apiNote This api is for searching product using subTitles
     * @param
     * @return
     */
    @GetMapping("/search/{subTitle}")
    public ResponseEntity<PageableResponse<ProductDto>> searchProduct(
            @RequestParam(value = "pageNumber",defaultValue = AppConstats.PAGE_NUMBER,required = false) int pageNumber,
            @RequestParam(value = "pageSize",defaultValue = AppConstats.PAGE_SIZE,required = false) int pageSize,
            @RequestParam(value = "sortBy",defaultValue = AppConstats.SORT_BY_CAT,required = false) String sortBy,
            @RequestParam(value = "sortDir",defaultValue =AppConstats.SORT_DIR,required = false) String sortDir,
            @PathVariable String subTitle)
    {
        logger.info("Request entering for searching Product with sub titles : {}",subTitle);
        PageableResponse<ProductDto> products = productService.searchByTitle(subTitle, pageNumber, pageSize, sortBy, sortDir);
        logger.info("Completed Request for searching  Product with sub titles : {}",subTitle);
        return new ResponseEntity<PageableResponse<ProductDto>>(products,HttpStatus.OK);
    }

}
