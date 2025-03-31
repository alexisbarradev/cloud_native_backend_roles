package com.backend.roles.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;
import java.time.ZoneId;

public class RoleAuditPayload {

    @JsonProperty("userRut")
    private String rut;

    @JsonProperty("previousRole")
    private String oldRole;

    @JsonProperty("currentRole")
    private String newRole;

    @JsonProperty("modifier")
    private String modifiedBy;

    @JsonProperty("auditTime")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private OffsetDateTime timestamp;

    // ⚠️ Constructor vacío requerido por Jackson
    public RoleAuditPayload() {
    }

    public RoleAuditPayload(String rut, String oldRole, String newRole, String modifiedBy) {
        this.rut = rut;
        this.oldRole = oldRole;
        this.newRole = newRole;
        this.modifiedBy = modifiedBy;
        // Establecer la zona horaria correcta
        this.timestamp = OffsetDateTime.now(ZoneId.of("America/Santiago")); // Reemplaza "America/Santiago" con tu zona horaria
    }

    // Getters
    public String getRut() {
        return rut;
    }

    public String getOldRole() {
        return oldRole;
    }

    public String getNewRole() {
        return newRole;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public OffsetDateTime getTimestamp() {
        return timestamp;
    }

    // Setters
    public void setRut(String rut) {
        this.rut = rut;
    }

    public void setOldRole(String oldRole) {
        this.oldRole = oldRole;
    }

    public void setNewRole(String newRole) {
        this.newRole = newRole;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public void setTimestamp(OffsetDateTime timestamp) {
        this.timestamp = timestamp;
    }
}