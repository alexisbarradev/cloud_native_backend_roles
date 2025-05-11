package com.backend.roles.model;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {

    @Id
    private String rut;

    private String username;
    private String password;
    private String email;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role; // Relación con entidad Role

    // Getters y Setters

    public String getRut() { return rut; }
    public void setRut(String rut) { this.rut = rut; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }
}
