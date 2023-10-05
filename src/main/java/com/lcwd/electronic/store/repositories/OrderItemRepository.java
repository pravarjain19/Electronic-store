package com.lcwd.electronic.store.repositories;

import com.lcwd.electronic.store.entites.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


public interface OrderItemRepository extends JpaRepository<OrderItem , String> {
}
