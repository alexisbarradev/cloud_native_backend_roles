package com.backend.roles.repository;

import com.backend.roles.model.User;
import com.backend.roles.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    // 🔧 Permite buscar todos los usuarios que tienen un rol específico
    List<User> findByRole(Role role);
}
