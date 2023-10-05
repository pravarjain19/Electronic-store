package com.lcwd.electronic.store.repositories;

import com.lcwd.electronic.store.entites.Cart;
import com.lcwd.electronic.store.entites.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart , String> {

    Optional<Cart> findByUser(User user);
}
