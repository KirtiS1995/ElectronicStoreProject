package com.electronic.store.services.impl;

import com.electronic.store.dtos.CategoryDto;
import com.electronic.store.dtos.PageableResponse;
import com.electronic.store.dtos.UserDto;
import com.electronic.store.entities.Category;
import com.electronic.store.entities.User;
import com.electronic.store.exceptions.ResourceNotFoundException;
import com.electronic.store.helper.AppConstats;
import com.electronic.store.repositories.CategoryRepository;
import com.electronic.store.services.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    @Override
    public CategoryDto create(CategoryDto categoryDto) {
        log.info("Entering DAO call for creating catagory ");
        Category category = mapper.map(categoryDto, Category.class);
        Category savedCategory = this.categoryRepo.save(category);
        log.info("Completed DAO call for creating category ");
        return mapper.map(savedCategory,CategoryDto.class);
    }

    @Override
    public CategoryDto updateCategory(CategoryDto categoryDto, String categoryId) {
        log.info("Entering DAO call for updating category with categoryId :{}", categoryId);
        Category category = categoryRepo.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException(AppConstats.CATEGPORY_NOT_FOUND + categoryId));
        category.setTitle(categoryDto.getTitle());
        category.setDescription(categoryDto.getDescription());
        category.setCoverImage(categoryDto.getCoverImage());

        Category uodatedCategory = categoryRepo.save(category);
        log.info("Completed DAO call for updating User  with categoryId :{}", categoryId);
        return mapper.map(uodatedCategory,CategoryDto.class);
    }

    @Override
    public void deleteCategory(String categoryId) {
        log.info("Entering DAO call for deleting category with categoryId :{}", categoryId);
        Category category = categoryRepo.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException(AppConstats.CATEGPORY_NOT_FOUND + categoryId));
        log.info("Completed DAO call for updating User  with categoryId :{}", categoryId);
        categoryRepo.delete(category);
    }

    @Override
    public PageableResponse<CategoryDto> getAllCategory(int pageNumber, int pageSize, String sortBy, String sortDir) {
        return null;
    }

    @Override
    public CategoryDto getSingleCategory(String categoryId) {

        log.info("Entering DAO call for getting Category with categoryId:{} ",categoryId);
        Category category = categoryRepo.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException(AppConstats.CATEGPORY_NOT_FOUND + categoryId));
        log.info("Completed DAO call for getting Category with categoryId:{} ",categoryId);
        return mapper.map(category,CategoryDto.class);
    }

    @Override
    public List<CategoryDto> searchCategory(String keyword) {
        log.info("Entering DAO call for searching Category with keyword:{} ",keyword);
        List<Category> categories = categoryRepo.findByTitleContaining(keyword);
         categories.stream().map(object -> mapper.ma(user)).collect(Collectors.toList());
        log.info("Completed DAO call for searching Category with keyword:{} ",keyword);
        return dtoList;
    }
}
