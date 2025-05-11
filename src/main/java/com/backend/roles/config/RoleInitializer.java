package com.backend.roles.config;

import com.backend.roles.model.Role;
import com.backend.roles.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RoleInitializer {

    @Bean
    public CommandLineRunner loadDefaultRoles(RoleRepository roleRepository) {
        return args -> {
            if (roleRepository.findByName("ADMIN").isEmpty()) {
                roleRepository.save(new Role("ADMIN"));
                System.out.println("✅ Rol ADMIN creado");
            }
            if (roleRepository.findByName("USER").isEmpty()) {
                roleRepository.save(new Role("USER"));
                System.out.println("✅ Rol USER creado");
            }
            if (roleRepository.findByName("INACTIVO").isEmpty()) {
                roleRepository.save(new Role("INACTIVO"));
                System.out.println("✅ Rol INACTIVO creado");
            }
        };
    }
}

