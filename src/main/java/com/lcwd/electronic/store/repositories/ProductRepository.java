package com.lcwd.electronic.store.repositories;

import com.lcwd.electronic.store.dtos.PageResponse;
import com.lcwd.electronic.store.entites.Category;
import com.lcwd.electronic.store.entites.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product , String> {


    Page<Product> findBytitleContaining(String keyword  , Pageable pageable);
    Page<Product> findByLiveTrue(Pageable pageable);

    Page<Product> findByCategory(Category category  , Pageable pageable);



}
