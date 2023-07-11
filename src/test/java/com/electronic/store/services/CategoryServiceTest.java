package com.electronic.store.services;

import com.electronic.store.dtos.CategoryDto;
import com.electronic.store.dtos.PageableResponse;
import com.electronic.store.dtos.UserDto;
import com.electronic.store.entities.Category;
import com.electronic.store.entities.User;
import com.electronic.store.repositories.CategoryRepository;
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
class CategoryServiceTest {

    @MockBean
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper mapper;

    //    @InjectMocks
    @Autowired
    private CategoryService categoryService;

    Category category;

    @BeforeEach
    public void init(){
        category =Category.builder()
                .title("TV")
                .description("Category realted to TV")
                .coverImage("xyz.png")
                .build();
    }
    @Test
    public void createCategoryTest() {
        Mockito.when(categoryRepository.save(Mockito.any())).thenReturn(category);
        CategoryDto categoryDto = categoryService.create(mapper.map(category, CategoryDto.class));
        System.out.println(categoryDto.getTitle());
        Assertions.assertNotNull(categoryDto);
        Assertions.assertEquals("TV",categoryDto.getTitle());
    }

    @Test
     public void updateCategory() {
        String  categoryId="catId";
                CategoryDto categoryDto=CategoryDto.builder()
                        .title("Tv")
                        .description("RElated to TV")
                        .coverImage("abc.png")
                        .build();

        Mockito.when(categoryRepository.findById(Mockito.anyString())).thenReturn(Optional.of(category));
        Mockito.when(categoryRepository.save(Mockito.any())).thenReturn(category);

       CategoryDto categoryDto1 = categoryService.updateCategory(categoryDto, categoryId);

        System.out.println(categoryDto1.getTitle());
        System.out.println(categoryDto1.getCoverImage());
        Assertions.assertNotNull(categoryDto1);
        Assertions.assertEquals(categoryDto.getTitle(),categoryDto1.getTitle(),"Title is not validated");
    }

    @Test
   public void deleteCategory() {
        String categoryId="catId1";
        Mockito.when(categoryRepository.findById("catId1")).thenReturn(Optional.of(category));

        categoryService.deleteCategory(categoryId);

        Mockito.verify(categoryRepository,Mockito.times(1)).delete(category);
    }

    @Test
   public void getAllCategory() {
        Category category1 =Category.builder()
                .title("Laptop")
                .description("Category related to Laptop")
                .coverImage("xyz.png")
                .build();
     Category category2 =Category.builder()
                .title("Mobile")
                .description("Category realted to Mobile")
                .coverImage("xyz.png")
                .build();

        List<Category> categoryList= Arrays.asList(category,category1,category2);

        Page<Category> page =new PageImpl<>(categoryList);

        Mockito.when(categoryRepository.findAll((Pageable) Mockito.any())).thenReturn(page);

        PageableResponse<CategoryDto> allCat = categoryService.getAllCategory(1,2,"title","asc");

        System.out.println("total category :"+allCat.getContent().size());
        Assertions.assertEquals(3,allCat.getContent().size());
    }

    @Test
   public void getSingleCategory() {
        String categoryId="categoryId";
        Mockito.when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));

        CategoryDto singleCategory = categoryService.getSingleCategory(categoryId);

        Assertions.assertNotNull(singleCategory);
        System.out.println(singleCategory.getTitle());
        Assertions.assertEquals(category.getTitle(),singleCategory.getTitle(),"Category not matched");
    }


    @Test
   public void searchCategory() {

        Category category1 =Category.builder()
                .title("Laptop ")
                .description("Category related to Laptop")
                .coverImage("xyz.png")
                .build();
        Category category2 =Category.builder()
                .title("Mobile category")
                .description("Category related to Mobile")
                .coverImage("xyz.png")
                .build();
        Category category3 =Category.builder()
                .title("Mobile category")
                .description("Category related to Mobile")
                .coverImage("bbb.png")
                .build();

        String keywords="category";

        Mockito.when(categoryRepository.findByTitleContaining(keywords)).thenReturn(Arrays.asList(category,category1,category2,category3));
        List<CategoryDto> categoryDtos = categoryService.searchCategory(keywords);
        System.out.println("Total Category Size  = "+categoryDtos.size());
        Assertions.assertEquals(4,categoryDtos.size(),"Size not matched !!");
    }

}