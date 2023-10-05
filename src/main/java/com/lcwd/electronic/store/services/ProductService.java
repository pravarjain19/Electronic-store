package com.lcwd.electronic.store.services;

import com.lcwd.electronic.store.dtos.PageResponse;
import com.lcwd.electronic.store.dtos.ProductDto;
import com.lcwd.electronic.store.entites.Category;

import java.io.FileNotFoundException;
import java.util.List;

public interface ProductService {

//    create
    ProductDto create(ProductDto productDto);
//    get single
    ProductDto getSingleProduct(String id);
//    get all
    PageResponse<ProductDto> getAllProduct(int pageSize , int pageNumber  , String sortBy , String sortDir);
//    update
    ProductDto updateProduct(ProductDto productDto , String id);

//    delete
    void  deleteProduct(String id) throws FileNotFoundException;

    PageResponse<ProductDto> getByProductByKeyword(String keyword , int pageSize , int pageNumber  , String sortBy , String sortDir);

    PageResponse<ProductDto> getAllLiveProduct(int pageSize , int pageNumber  , String sortBy , String sortDir);


//     create product with categories

    ProductDto createWithCategory(ProductDto productDto , String categoryId);

    ProductDto updateCategory(String categoryId , String productId);

    PageResponse<ProductDto> getAllProductByCategory(String cid , int pageSize , int pageNumber  , String sortBy , String sortDir) ;
}
