package com.electronic.store.services;

import com.electronic.store.dtos.CategoryDto;
import com.electronic.store.dtos.PageableResponse;
import com.electronic.store.dtos.ProductDto;
import com.electronic.store.dtos.UserDto;
import com.electronic.store.entities.Category;
import com.electronic.store.entities.Product;
import com.electronic.store.entities.User;
import com.electronic.store.exceptions.ResourceNotFoundException;
import com.electronic.store.repositories.CategoryRepository;
import com.electronic.store.repositories.ProductRepository;
import com.electronic.store.repositories.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ProductServiceTest {
    @MockBean
    private ProductRepository productRepository;

    @MockBean
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private ProductService productService;

  Product product;

  Category category;

    Product product1= Product.builder()
            .title("Iphone")
            .description("Phone having good camera")
            .price(120000)
            .discountedPrice(10000)
            .quantity(40)
            .live(true)
            .stock(false)
            .productImage("xyz.png")
            .build();
    Product product2 = Product.builder()
            .title("MI")
            .description("Phone having good camera")
            .price(120000)
            .discountedPrice(10000)
            .quantity(40)
            .live(true)
            .stock(false)
            .productImage("abc.png")
            .build();
    Product product3= Product.builder()
            .title("Iphone")
            .description("Phone having good camera")
            .price(120000)
            .discountedPrice(10000)
            .quantity(40)
            .live(true)
            .stock(false)
            .productImage("xyz.png")
            .build();

    @BeforeEach
    public void init(){
        product = Product.builder()
                .title("Samsung")
                .description("Phone having good camera")
                .price(120000)
                .discountedPrice(10000)
                .quantity(40)
                .live(true)
                .stock(false)
                .productImage("abc.png")
                .build();
        Category  category = Category.builder()
                .title("TV")
                .description("Category related to TV")
                .coverImage("xyz.png")
                .build();
         }

    @Test
   public void createProductTest() {
        Mockito.when(productRepository.save(Mockito.any())).thenReturn(product);
        ProductDto product1 = productService.createProduct(mapper.map(product, ProductDto.class));
        System.out.println(product1.getTitle());
        Assertions.assertNotNull(product1);
        Assertions.assertEquals("Samsung",product1.getTitle());
    }

    @Test
    public void updateProductTest() {
        String  productId="productId";
        ProductDto productDto = ProductDto.builder()
                .title("Iphone")
                .description("Phone having good camera")
                .price(120000)
                .discountedPrice(10000)
                .quantity(40)
                .live(true)
                .stock(false)
                .productImage("xyz.png")
                .build();

        Mockito.when(productRepository.findById(Mockito.anyString())).thenReturn(Optional.of(product));
        Mockito.when(productRepository.save(Mockito.any())).thenReturn(product);

        ProductDto productDto1 = productService.updateProduct(productDto, productId);

        System.out.println(productDto1.getTitle());
        System.out.println(productDto1.getProductImage());
        Assertions.assertNotNull(productDto1);
        Assertions.assertEquals(productDto.getTitle(),productDto1.getTitle(),"Title is not validated");
    }

    @Test
    public void deleteProductTest() {
        String productId="id1";
        Mockito.when(productRepository.findById("id1")).thenReturn(Optional.of(product));
        productService.deleteProduct(productId);
        Mockito.verify(productRepository,Mockito.times(1)).delete(product);
    }

    @Test
    public void getSingleProductTest() {
        String productId="id1";
        Mockito.when(productRepository.findById("id1")).thenReturn(Optional.of(product));

        ProductDto singleProduct = productService.getSingleProduct(productId);

        Assertions.assertNotNull(singleProduct);
        System.out.println(singleProduct.getTitle());
        Assertions.assertEquals(product.getTitle(),singleProduct.getTitle(),"Title not matched");
    }

    @Test
    public void getAllTest() {
        List<Product> list = Arrays.asList(product, product1, product2);
        Page<Product> page =new PageImpl<>(list);

        Mockito.when(productRepository.findAll((Pageable) Mockito.any())).thenReturn(page);
        PageableResponse<ProductDto> allProduct = productService.getAll(1,2,"title","asc");

        System.out.println("total product :"+allProduct.getContent().size());
        Assertions.assertEquals(3,allProduct.getContent().size());
    }

    @Test
    public void getAllLiveTest() {
        List<Product> list = Arrays.asList(product, product1, product2);
        Page<Product> page =new PageImpl<>(list);
        Mockito.when(productRepository.findAll((Pageable) Mockito.any())).thenReturn(page);

        Mockito.when(productRepository.findByLiveTrue(Mockito.any())).thenReturn(page);
        PageableResponse<ProductDto> allLive = productService.getAllLive(1, 2, "name", "asc");
        System.out.println("Total no of live record find ="+allLive.getContent().size());
        Assertions.assertEquals(3,allLive.getContent().size(),"Size not matched.!!");

    }

    @Test
    public void searchByTitleTest() {
        String keywords="phone";
        List<Product> list = Arrays.asList(product, product1, product2, product3);
        Page<Product> page =new PageImpl<>(list);

        Mockito.when(productRepository.findAll((Pageable) Mockito.any())).thenReturn(page);

        Mockito.when(productRepository.findByTitleContaining( Mockito.anyString(), Mockito.any())).thenReturn(page);
        PageableResponse<ProductDto> pageableResponse = productService.searchByTitle(keywords,1,2,"name","asc");
            System.out.println("Total no of record find ="+pageableResponse.getContent().size());
        Assertions.assertEquals(4,pageableResponse.getContent().size(),"Size not matched.!!");

    }

    @Test
    public void createWithCategoryTest() {
        String categoryId="catId";

        Category  category = Category.builder()
                .title("Mobile")
                .description("Category related to mobile")
                .coverImage("xyz.png")
                .build();

//        Mockito.when(categoryRepository.findById(Mockito.anyString())).thenReturn(Optional.of(category));
        Mockito.when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));

        Mockito.when(productRepository.save(Mockito.any())).thenReturn(product);
        ProductDto productdto = this.mapper.map(product, ProductDto.class);
        ProductDto productDto1 = productService.createWithCategory(productdto, categoryId);
        assertNotNull(productDto1);
        System.out.println(productDto1.getTitle());
        assertEquals(productDto1.getPrice(),productdto.getPrice(),"Failed ");
        Assertions.assertThrows(ResourceNotFoundException.class, () -> productService.createWithCategory(productdto,"categoryId"));
    }

    @Test
    public void updateCategoryWithProductTest() {
        String  productId="123";
        String categoryId="12345";

        Category category1 = Category.builder()
                .title("Mobile")
                .description("Category related to mobile")
                .coverImage("xyz.png")
                .build();
        Product product4 = Product.builder()
                .title("Iphone")
                .description("Phone having good camera")
                .price(1230000)
                .discountedPrice(10000)
                .quantity(40)
                .live(true)
                .stock(false)
                .productImage("xyz.png")
                .category(category)
                .build();

        Mockito.when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        Mockito.when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category1));
        Mockito.when(productRepository.save(Mockito.any())).thenReturn(product4);

        ProductDto productDto1 = productService.updateCategory(productId, categoryId);

        System.out.println(productDto1.getPrice());
        Assertions.assertNotNull(productDto1);
        Assertions.assertEquals(category1.getCategoryId(),productDto1.getProductId());
        Assertions.assertThrows(ResourceNotFoundException.class, () -> productService.updateCategory(productId,"1234"));
    }


}