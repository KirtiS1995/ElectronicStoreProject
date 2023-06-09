package com.electronic.store.services.impl;

import com.electronic.store.dtos.CategoryDto;
import com.electronic.store.dtos.PageableResponse;
import com.electronic.store.entities.Category;
import com.electronic.store.exceptions.ResourceNotFoundException;
import com.electronic.store.helper.AppConstats;
import com.electronic.store.helper.Helper;
import com.electronic.store.repositories.CategoryRepository;
import com.electronic.store.services.CategoryService;
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
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepo;

    @Autowired
    private ModelMapper mapper;

    @Value("${category.image.path}")
    private String imagePath;

    @Override
    public CategoryDto create(CategoryDto categoryDto) {
        log.info("Entering DAO call for creating cateory ");
        String categoryId = UUID.randomUUID().toString();
        categoryDto.setCategoryId(categoryId);
        //Convert Dto to entity
        Category category = mapper.map(categoryDto, Category.class);
        Category savedCategory = this.categoryRepo.save(category);
        log.info("Completed DAO call for creating category ");
        return mapper.map(savedCategory,CategoryDto.class);
    }

    @Override
    public CategoryDto updateCategory(CategoryDto categoryDto, String categoryId) {
        log.info("Entering DAO call for updating category with categoryId :{}", categoryId);
        Category category = categoryRepo.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException(AppConstats.CATEGORY_NOT_FOUND + categoryId));
        category.setTitle(categoryDto.getTitle());
        category.setDescription(categoryDto.getDescription());
        category.setCoverImage(categoryDto.getCoverImage());
        category.setLastModifiedBy(categoryDto.getLastModifiedBy());

        Category uodatedCategory = categoryRepo.save(category);
        log.info("Completed DAO call for updating User  with categoryId :{}", categoryId);
        return mapper.map(uodatedCategory,CategoryDto.class);
    }

    @Override
    public void deleteCategory(String categoryId) {
        log.info("Entering DAO call for deleting category with categoryId :{}", categoryId);
        Category category = categoryRepo.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException(AppConstats.CATEGORY_NOT_FOUND + categoryId));

        //   images/user/abc.png
        String fullPath = imagePath + category.getCoverImage();
        try{
            Path path = Paths.get(fullPath);
            Files.delete(path);
        }catch (NoSuchFileException ex){
            log.info("Category Cover Image not found in folder");
            ex.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }

        log.info("Completed DAO call for deleting User  with categoryId :{}", categoryId);
        categoryRepo.delete(category);
    }

    @Override
    public PageableResponse<CategoryDto> getAllCategory(int pageNumber, int pageSize, String sortBy, String sortDir) {
        log.info("Entering DAO call for getting all Category with pageNumber :{} ",pageNumber);
        Sort sort=(sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());
        Pageable pageable= PageRequest.of(pageNumber,pageSize,sort);

        Page<Category> categories = categoryRepo.findAll(pageable);
        PageableResponse<CategoryDto> response = Helper.getPageableResponse(categories, CategoryDto.class);

        log.info("Completed DAO call for getting all Category with pageNumber :{} ",pageNumber );
        return response;
    }

    @Override
    public CategoryDto getSingleCategory(String categoryId) {
        log.info("Entering DAO call for getting Category with categoryId:{} ",categoryId);
        Category category = categoryRepo.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException(AppConstats.CATEGORY_NOT_FOUND + categoryId));
        log.info("Completed DAO call for getting Category with categoryId:{} ",categoryId);
        return mapper.map(category,CategoryDto.class);
    }
    @Override
    public List<CategoryDto> searchCategory(String keyword) {
        log.info("Entering DAO call for searching Category with keyword:{} ",keyword);
        List<Category> categories = categoryRepo.findByTitleContaining(keyword);
        List<CategoryDto> categoryDtoList = categories.stream().map((cat) -> this.mapper.map(cat, CategoryDto.class)).collect(Collectors.toList());
        log.info("Completed DAO call for searching Category with keyword:{} ",keyword);
        return categoryDtoList;
    }
}
