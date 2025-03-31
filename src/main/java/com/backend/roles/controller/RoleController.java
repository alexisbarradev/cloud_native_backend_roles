package com.backend.roles.controller;

import com.backend.roles.service.RoleService;
import com.backend.roles.service.AuditSenderService; // Importa AuditSenderService
import com.backend.roles.model.User;
import com.backend.roles.model.RoleAuditPayload; // Importa RoleAuditPayload
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/roles")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @Autowired
    private AuditSenderService auditSenderService; // Inyecta AuditSenderService

    @PutMapping("/{rut}")
    public ResponseEntity<String> updateUserRole(@PathVariable String rut, @RequestParam String newRole) {
        Optional<User> userOpt = roleService.findUserByRut(rut);

        if (userOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Usuario no encontrado.");
        }

        User user = userOpt.get();
        String oldRole = user.getRole();

        // Verificar si el rol realmente cambió
        if (oldRole.equalsIgnoreCase(newRole)) {
            return ResponseEntity.ok("El nuevo rol es igual al actual. No se realizó ningún cambio.");
        }

        // Actualizar el rol
        boolean updated = roleService.updateRole(rut, newRole);

        if (updated) {
            // ✅ Crear el RoleAuditPayload
            RoleAuditPayload auditPayload = new RoleAuditPayload(rut, oldRole, newRole, "admin"); // "admin" debe ser dinámico si hay autenticación

            // ✅ Utilizar el AuditSenderService para enviar la auditoría
            auditSenderService.sendAudit(auditPayload);

            return ResponseEntity.ok("Rol actualizado correctamente.");
        } else {
            return ResponseEntity.status(500).body("Error al actualizar el rol.");
        }
    }
}