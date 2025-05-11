package com.backend.roles.repository;

import com.backend.roles.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    // ✅ Devuelve un Optional para permitir verificación con isEmpty()
    Optional<Role> findByName(String name);
}
