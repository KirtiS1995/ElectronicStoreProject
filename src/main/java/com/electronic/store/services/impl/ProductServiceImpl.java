package com.electronic.store.services.impl;

import com.electronic.store.dtos.PageableResponse;
import com.electronic.store.dtos.ProductDto;
import com.electronic.store.dtos.UserDto;
import com.electronic.store.entities.Category;
import com.electronic.store.entities.Product;
import com.electronic.store.entities.User;
import com.electronic.store.exceptions.ResourceNotFoundException;
import com.electronic.store.helper.AppConstats;
import com.electronic.store.helper.Helper;
import com.electronic.store.repositories.CategoryRepository;
import com.electronic.store.repositories.ProductRepository;
import com.electronic.store.services.ProductService;
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
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepo;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private CategoryRepository categoryRepo;

    @Value("${product.image.path}")
    private String imagePath;

    /**
     * * @author kirti
     * @implNote  This method is for Creating Product
     * @param productDto
     * @return
     */
    @Override
    public ProductDto createProduct(ProductDto productDto) {
        log.info("Request entering DAO call for creating product ");

        String productId = UUID.randomUUID().toString();
        productDto.setProductId(productId);
        productDto.setAddedDate(new Date());

        Product product = mapper.map(productDto, Product.class);
        Product savedProduct = this.productRepo.save(product);
        log.info("Request completed DAO call for creating product ");
        return mapper.map(savedProduct,ProductDto.class);
    }

    /**
     *  @author kirti
     * @implNote  This method is for Updating product
     * @param productDto
     * @param productId
     * @return
     */
    @Override
    public ProductDto updateProduct(ProductDto productDto, String productId) {
        log.info("Entering DAO call for updating Product  with productId :{}",productId);
        //Fetch product of given Id
        Product product = productRepo.findById(productId).orElseThrow(() -> new ResourceNotFoundException(AppConstats.PRODUCT_NOT_FOUND + productId));
        product.setTitle(productDto.getTitle());
        product.setDescription(productDto.getDescription());
        product.setPrice(productDto.getPrice());
        product.setDiscountedPrice(productDto.getDiscountedPrice());
        product.setQuantity(productDto.getQuantity());
        product.setLive(productDto.isLive());
        product.setStock(productDto.isStock());
        product.setLastModifiedBy(productDto.getLastModifiedBy());
        product.setProductImage(productDto.getProductImage());

        //save
        Product updatedProduct = productRepo.save(product);
        log.info("Completed DAO call for updating Product  with productId :{}",productId);
        return mapper.map(updatedProduct,ProductDto.class);
    }
    /**
     * @author kirti
     * @implNote  This method is for delete Product by productId
     * @param productId
     * @return
     */
    @Override
    public void deleteProduct(String productId) {
        log.info("Request entering DAO call for deleting Product  with productId :{}",productId);
        Product product = productRepo.findById(productId).orElseThrow(() -> new ResourceNotFoundException(AppConstats.PRODUCT_NOT_FOUND + productId));
        //   images/user/abc.png
        String fullPath = imagePath + product.getProductImage();
        try{
            Path path = Paths.get(fullPath);
            Files.delete(path);
        }catch (NoSuchFileException ex){
            log.info("Product  Image not found in folder");
            ex.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
        productRepo.delete(product);
            log.info("Completed DAO call for deleting Product  with productId :{}",productId);
    }
    /**
     * * @author kirti
     * @implNote  This method is for Getting Single Product By productId
     * @param productId
     * @return
     */
    @Override
    public ProductDto getSingleProduct(String productId) {
        log.info("Request entering DAO call for getting single Product with productId:{} ",productId);
        Product product = productRepo.findById(productId).orElseThrow(() -> new ResourceNotFoundException(AppConstats.PRODUCT_NOT_FOUND + productId));
        log.info("Entering DAO call for getting single Product with productId:{} ",productId);
        return mapper.map(product,ProductDto.class);
    }

    /**
     * @author kirti
     * @implNote  This Api is for Getting ALL Product
     * @return
     */
    @Override
    public PageableResponse<ProductDto> getAll(int pageNumber, int pageSize, String sortBy, String sortDir) {
        log.info("Entering DAO call for getting all Product with pageNumber:{}",pageNumber);

        Sort sort=(sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());
        //default page no starts from zero..
        //pagenumber+1 for starting from 1
        Pageable pageable= PageRequest.of(pageNumber,pageSize,sort);

        Page<Product> allProduct = productRepo.findAll(pageable);
        PageableResponse<ProductDto> response = Helper.getPageableResponse(allProduct, ProductDto.class);

        log.info("Completed DAO call for getting all Product with pageNumber :{} ",pageNumber );
        return response;
    }
        /**
        * @author kirti
     * @implNote  This Api is for Getting ALL Live Product
     * @return
             */
    @Override

    public PageableResponse<ProductDto> getAllLive(int pageNumber, int pageSize, String sortBy, String sortDir) {

        log.info("Entering DAO call for getting all Live Product with pageNumber :{} ",pageNumber);

        Sort sort=(sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());
        Pageable pageable= PageRequest.of(pageNumber,pageSize,sort);
        Page<Product> liveProduct = productRepo.findByLiveTrue(pageable);
        PageableResponse<ProductDto> response = Helper.getPageableResponse(liveProduct, ProductDto.class);
        log.info("Completed DAO call for getting all Live Product with pageNumber :{} ",pageNumber );
        return response;
    }
    /**
     *  @author kirti
     * @implNote  This api is for searching Product using sub Titles
     * @param subTitle
     * @return
     */
    @Override
    public PageableResponse<ProductDto> searchByTitle(String subTitle,int pageNumber, int pageSize, String sortBy, String sortDir) {
        log.info("Entering DAO call for searching Product with subTitle:{} ",subTitle);
        Sort sort=(sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());
        Pageable pageable= PageRequest.of(pageNumber,pageSize,sort);
        Page<Product> products = productRepo.findByTitleContaining(subTitle, pageable);
        PageableResponse<ProductDto> response = Helper.getPageableResponse(products, ProductDto.class);
        log.info("Completed DAO call for searching User with subTitle:{} ",subTitle);
        return response;
    }
    /**
     *  @author kirti
     * @implNote  This api is for creating Product with categoryId
     * @param productDto
     * @param  categoryId
     * @return
     */
    @Override
    public ProductDto createWithCategory(ProductDto productDto, String categoryId) {
        log.info("Request completed DAO call for creating product with categoryId  :{} ",categoryId);
        //fetch the category from db
        Category category = categoryRepo.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException(AppConstats.CATEGORY_NOT_FOUND + categoryId));
        Product product = mapper.map(productDto, Product.class);

        String productId = UUID.randomUUID().toString();
        product.setProductId(productId);
        product.setAddedDate(new Date());
        product.setCategory(category);
        Product savedProduct = this.productRepo.save(product);
        log.info("Request completed DAO call for creating product with categoryId :{} ",categoryId);
        return mapper.map(savedProduct,ProductDto.class);
    }

    /**
     * @implNote This method is for updating category with productId
     * @param productId
     * @param categoryId
     * @return
     */
    @Override
    public ProductDto updateProductWithCategory(String productId, String categoryId) {

        log.info("Request Entering DAO call for Updating category of product with categoryId :{} ",categoryId);
        //product fetch
        Product product = productRepo.findById(productId).orElseThrow(() -> new ResourceNotFoundException(AppConstats.PRODUCT_NOT_FOUND + productId));
        Category category = categoryRepo.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException(AppConstats.CATEGORY_NOT_FOUND + categoryId));
        product.setCategory(category);
        Product savedProduct = productRepo.save(product);

        log.info("Request completed DAO call for Updating category of product with categoryId :{} ",categoryId);

        return mapper.map(savedProduct,ProductDto.class);
    }

    /**
     * * @implNote This method is for getting all product of category
     * @param categoryId
     * @return
     */
    @Override
    public PageableResponse<ProductDto> getAllOfCategory(String categoryId,int pageNumber, int pageSize, String sortBy, String sortDir) {
        log.info("Request Entering DAO call for getting product  of similar category with categoryId :{} ",categoryId);

        Category category = categoryRepo.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException(AppConstats.CATEGORY_NOT_FOUND + categoryId));
        Sort sort=(sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());
        Pageable pageable= PageRequest.of(pageNumber,pageSize,sort);
        Page<Product> page = productRepo.findByCategory(category,pageable);
        log.info("Request completed DAO call forgetting product  of similar category  with categoryId :{} ",categoryId);
        return Helper.getPageableResponse(page,ProductDto.class);
    }

}
