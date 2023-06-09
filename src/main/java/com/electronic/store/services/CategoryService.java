package com.electronic.store.services;

import com.electronic.store.dtos.CategoryDto;
import com.electronic.store.dtos.PageableResponse;
import com.electronic.store.dtos.UserDto;

import java.util.List;

public interface CategoryService {

    //create
    CategoryDto create(CategoryDto categoryDto);

    //update
    CategoryDto updateCategory(CategoryDto categoryDto,String categoryId);

    //delete
    void deleteCategory(String categoryId);

    //getall category
    PageableResponse<CategoryDto> getAllCategory(int pageNumber, int pageSize, String sortBy, String sortDir);

    //get single category
    CategoryDto getSingleCategory(String categoryId);

    //search category
    List<CategoryDto> searchCategory(String keyword);

    //other user specific features

}
