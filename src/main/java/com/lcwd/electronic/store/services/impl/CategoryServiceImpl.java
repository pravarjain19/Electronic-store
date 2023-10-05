package com.lcwd.electronic.store.services.impl;

import com.lcwd.electronic.store.dtos.CategoryDto;
import com.lcwd.electronic.store.dtos.PageResponse;
import com.lcwd.electronic.store.entites.Category;
import com.lcwd.electronic.store.exceptions.ResourceNotFoundException;
import com.lcwd.electronic.store.helper.Helper;
import com.lcwd.electronic.store.repositories.CategoryRepository;
import com.lcwd.electronic.store.services.CategoryService;
import com.lcwd.electronic.store.services.FileService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;


    @Override
    public CategoryDto create(CategoryDto categoryDto) {
        String cid = UUID.randomUUID().toString();
        categoryDto.setCategoryId(cid);
        Category category = modelMapper.map(categoryDto,Category.class);

        Category savedCategory  = categoryRepository.save(category);
        return modelMapper.map(savedCategory , CategoryDto.class);
    }

    @Override
    public CategoryDto update(CategoryDto categoryDto , String id) {

        Category category = categoryRepository.findById(id).
                orElseThrow(()-> new ResourceNotFoundException("Category Not found Exception"));
        category.setDescription(categoryDto.getDescription());
        category.setTitle(categoryDto.getTitle());
        category.setCoverImage(categoryDto.getCoverImage());

        Category save = categoryRepository.save(category);

        return modelMapper.map(save , CategoryDto.class);
    }

    @Override
    public void delete(String categoryId) throws FileNotFoundException {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() ->
                new ResourceNotFoundException("No category found by the id"));
        fileService.deleteFile(imageUploadPath , category.getCoverImage());
        categoryRepository.delete(category);

    }

    @Override
    public PageResponse<CategoryDto> getAll(int pageSize , int pageNumber  , String sortBy , String sortDir) {
        Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending())
                : (Sort.by(sortBy).ascending());
        Pageable pageable = PageRequest.of(pageNumber , pageSize , sort);

        Page<Category> page = categoryRepository.findAll(pageable);

        PageResponse<CategoryDto> pageableResponse = Helper.getPageableResponse(page, CategoryDto.class);
        return pageableResponse;
    }

    @Override
    public CategoryDto get(String categoryId) {

        return modelMapper.map(categoryRepository.findById(categoryId)
                .orElseThrow(()-> new ResourceNotFoundException("No user found " + categoryId))  , CategoryDto.class );
    }

    @Override
    public List<CategoryDto> searchCategory(String keyword) {
        List<Category> bytitleContaining = categoryRepository.findBytitleContaining(keyword);
       return bytitleContaining.stream().map(e -> modelMapper
                .map(e, CategoryDto.class)).collect(Collectors.toList());

    }
    private final ModelMapper modelMapper = new ModelMapper();

    @Autowired
    FileService fileService;

    @Value("${category.image.path}")
    private  String imageUploadPath;
}
