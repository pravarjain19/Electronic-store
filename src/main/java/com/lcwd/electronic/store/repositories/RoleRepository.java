package com.lcwd.electronic.store.repositories;

import com.lcwd.electronic.store.entites.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role , String> {
}
