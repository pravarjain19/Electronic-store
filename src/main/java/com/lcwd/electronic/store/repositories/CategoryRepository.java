package com.lcwd.electronic.store.repositories;

import com.lcwd.electronic.store.entites.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface CategoryRepository extends JpaRepository<Category , String> {

        List<Category> findBytitleContaining(String keyword);
}
