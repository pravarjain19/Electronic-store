package com.lcwd.electronic.store.services;

import com.lcwd.electronic.store.dtos.CategoryDto;
import com.lcwd.electronic.store.dtos.PageResponse;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.util.List;

public interface CategoryService {
//    create

        CategoryDto create(CategoryDto categoryDto);
//    updaate

        CategoryDto update(CategoryDto categoryDto ,String id);
//    delete
        void delete(String categoryId) throws FileNotFoundException;
//    get all

        PageResponse<CategoryDto> getAll(int pageSize , int pageNumber  , String sortBy , String sortDir);
//    get single category

    CategoryDto get(String categoryId);

    List<CategoryDto> searchCategory(String keyword);
}
