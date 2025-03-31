package com.backend.roles.model;


import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "role_audit")
public class RoleAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String rut;

    @Column(name = "old_role")
    private String oldRole;

    @Column(name = "new_role")
    private String newRole;

    @Column(name = "modified_by")
    private String modifiedBy;

    private LocalDateTime timestamp;

    // Default constructor required by JPA
    public RoleAudit() {}

    // Convenient constructor
    public RoleAudit(String rut, String oldRole, String newRole, String modifiedBy) {
        this.rut = rut;
        this.oldRole = oldRole;
        this.newRole = newRole;
        this.modifiedBy = modifiedBy;
        this.timestamp = LocalDateTime.now();
    }

    // Getters and setters
    public Long getId() { return id; }

    public String getRut() { return rut; }
    public void setRut(String rut) { this.rut = rut; }

    public String getOldRole() { return oldRole; }
    public void setOldRole(String oldRole) { this.oldRole = oldRole; }

    public String getNewRole() { return newRole; }
    public void setNewRole(String newRole) { this.newRole = newRole; }

    public String getModifiedBy() { return modifiedBy; }
    public void setModifiedBy(String modifiedBy) { this.modifiedBy = modifiedBy; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}
