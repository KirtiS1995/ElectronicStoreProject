package com.electronic.store.controllers;

import com.electronic.store.dtos.CategoryDto;
import com.electronic.store.dtos.PageableResponse;
import com.electronic.store.dtos.ProductDto;
import com.electronic.store.entities.Category;
import com.electronic.store.entities.Product;
import com.electronic.store.repositories.CategoryRepository;
import com.electronic.store.services.CategoryService;
import com.electronic.store.services.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import java.util.Arrays;
import java.util.List;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CategoryControllerTest {

    private Category category;
    private Product product;

     @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @Autowired
    private ModelMapper mapper;

    @MockBean
    private CategoryService categoryService;

    @BeforeEach
    public void init(){
        category =Category.builder()
                .title("TV")
                .description("Category realted to TV")
                .coverImage("xyz.png")
                .build();
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
    }
    @Test
    void createTest() throws Exception {
        CategoryDto dto = mapper.map(category, CategoryDto.class);
        Mockito.when(categoryService.create(Mockito.any())).thenReturn(dto);
        //actual request for url
        this.mockMvc.perform(
                        MockMvcRequestBuilders.post("/categories/")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(convertObjectToJsonString(category))
                                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").exists());
    }

    @Test
    void updateCategoryTest() throws Exception {
        String categoryId ="786";
       CategoryDto category1 =CategoryDto.builder()
                .title("Mobile")
                .description("Category related to Mobile")
                .coverImage("mb.png")
                .build();
        Mockito.when(categoryService.updateCategory(Mockito.any(),Mockito.anyString())).thenReturn(category1);
        this.mockMvc.perform(
                        MockMvcRequestBuilders.put("/categories/" +categoryId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(convertObjectToJsonString(category1))
                                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").exists());
            }

    @Test
    void getSingleCategoryTest() throws Exception {

        String categoryId ="555";
        CategoryDto categoryDto = this.mapper.map(category, CategoryDto.class);
        Mockito.when(categoryService.getSingleCategory(Mockito.anyString())).thenReturn(categoryDto);
//        Mockito.when(userService.getUserById(userId)).thenReturn(userDto);
        this.mockMvc.perform(
                        MockMvcRequestBuilders.get("/categories/"+categoryId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").exists());

    }

    @Test
    void deleteCategoryTest() throws Exception {
        String categoryId= "12345";
        Mockito.doNothing().when(categoryService).deleteCategory(Mockito.anyString());
        this.mockMvc.perform(
                        MockMvcRequestBuilders.delete("/categories/" +categoryId))
                .andDo(print())
                .andExpect(status().isOk());
        //verify
        Mockito.verify(categoryService,Mockito.times(1)).deleteCategory(categoryId);
    }

    @Test
    void searchCategoryTest() throws Exception {
        String keyword= "category";
        CategoryDto category2 =CategoryDto.builder()
                .title("Mobile")
                .description("Category related to Mobile")
                .coverImage("mb.png")
                .build();
        CategoryDto category3 =CategoryDto.builder()
                .title("Camera")
                .description("Category related to Camera")
                .coverImage("cams.png")
                .build();
        CategoryDto category4 =CategoryDto.builder()
                .title("Speaker")
                .description("Category related to Spakers")
                .coverImage("sp.png")
                .build();
        Mockito.when(categoryService.searchCategory(keyword)).thenReturn(List.of(category2,category3,category4));
        this.mockMvc.perform(
                        MockMvcRequestBuilders.get("/categories/search/"+keyword)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void getAllCategoriesTest() throws Exception {
        CategoryDto category2 =CategoryDto.builder()
                .title("Mobile")
                .description("Category related to Mobile")
                .coverImage("mb.png")
                .build();
        CategoryDto category3 =CategoryDto.builder()
                .title("Camera")
                .description("Category related to Camera")
                .coverImage("cams.png")
                .build();
        CategoryDto category4 =CategoryDto.builder()
                .title("Speaker")
                .description("Category related to Spakers")
                .coverImage("sp.png")
                .build();

        PageableResponse<CategoryDto> pageableResponse= new PageableResponse<>();

        pageableResponse.setLastPage(false);
        pageableResponse.setTotalElements(2000);
        pageableResponse.setPageNumber(50);
        pageableResponse.setContent(Arrays.asList(category2,category3,category4));
        pageableResponse.setTotalPages(200);
        pageableResponse.setPageSize(20);

        Mockito.when(categoryService.getAllCategory(Mockito.anyInt(),Mockito.anyInt(),Mockito.anyString(),Mockito.anyString())).thenReturn(pageableResponse);

        //request for url
        this.mockMvc.perform(
                        MockMvcRequestBuilders.get("/categories")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void createProductWithCategory() throws Exception {
        String categoryId="abc";
        ProductDto dto = mapper.map(product, ProductDto.class);
        Mockito.when(productService.createWithCategory(Mockito.any(),Mockito.anyString())).thenReturn(dto);
        //actual request for url
        this.mockMvc.perform(
                        MockMvcRequestBuilders.post("/categories/"+categoryId+"/products")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(convertObjectToJsonString(product))
                                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").exists());
    }
    @Test
    public void updateCategoryOfProduct() throws Exception {
        String categoryId="abc";
        String productId="12345";
        CategoryDto category3 =CategoryDto.builder()
                .title("mobile")
                .description("Category related to mobile")
                .coverImage("Mob.png")
                .build();
        ProductDto productDto = ProductDto.builder()
                .title("Iphone")
                .description("Phone having good camera")
                .price(120000)
                .discountedPrice(10000)
                .quantity(40)
                .live(true)
                .stock(false)
                .productImage("xyz.png")
                .category(category3)
                .build();
        Mockito.when(productService.updateCategory(Mockito.anyString(),Mockito.anyString())).thenReturn(productDto);
        this.mockMvc.perform(
                        MockMvcRequestBuilders.put("/categories/"+categoryId+"/products/"+productId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(convertObjectToJsonString(product))
                                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }
    @Test
    void getProductOfCategoryTest() throws Exception {
        String categoryId="abc";
        CategoryDto category =CategoryDto.builder()
                .title("mobile")
                .description("Category related to mobile")
                .coverImage("Mob.png")
                .build();
        ProductDto product1= ProductDto.builder()
                .title("Iphone")
                .description("Phone having good camera")
                .price(120000)
                .discountedPrice(10000)
                .quantity(40)
                .live(true)
                .stock(false)
                .productImage("xyz.png")
                .category(category)
                .build();
        ProductDto product2 = ProductDto.builder()
                .title("MI")
                .description("Phone having good camera")
                .price(120000)
                .discountedPrice(10000)
                .quantity(40)
                .live(true)
                .stock(false)
                .productImage("abc.png")
                .category(category)
                .build();
        ProductDto product3= ProductDto.builder()
                .title("Iphone")
                .description("Phone having good camera")
                .price(120000)
                .discountedPrice(10000)
                .quantity(40)
                .live(true)
                .stock(false)
                .productImage("xyz.png")
                .category(category)
                .build();

        PageableResponse<ProductDto> pageableResponse= new PageableResponse<>();

        pageableResponse.setLastPage(false);
        pageableResponse.setTotalElements(2000);
        pageableResponse.setPageNumber(50);
        pageableResponse.setContent(Arrays.asList(product1,product2,product3));
        pageableResponse.setTotalPages(200);
        pageableResponse.setPageSize(20);
        Mockito.when(productService.getAllOfCategory(Mockito.anyString(),Mockito.anyInt(),Mockito.anyInt(),Mockito.anyString(),Mockito.anyString())).thenReturn(pageableResponse);
        //request for url
        this.mockMvc.perform(
                        MockMvcRequestBuilders.get("/categories/"+categoryId+"/products")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    private String convertObjectToJsonString(Object user) {
        try {
            return new ObjectMapper().writeValueAsString(user);
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}